package co.com.powerup2025.api.handler;

import co.com.powerup2025.api.dtos.response.LoanResponse;
import co.com.powerup2025.api.dtos.response.PageableResponse;
import co.com.powerup2025.api.mapper.LoanMapper;
import co.com.powerup2025.api.dtos.request.LoanRequest;
import co.com.powerup2025.model.loans.Loan;
import co.com.powerup2025.model.logger.LoggerFactoryPort;
import co.com.powerup2025.model.logger.LoggerRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.powerup2025.model.loans.gateways.ILoanUseCase;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class LoanHandler {


    private final ILoanUseCase ILoanUseCase;
    private final LoggerRepository logger;
    private final LoanMapper mapper;
    private final co.com.powerup2025.api.handlers.ReactiveErrorHandler errorHelper;

    public LoanHandler(ILoanUseCase ILoanUseCase, LoggerFactoryPort loggerFactoryPort, LoanMapper mapper, co.com.powerup2025.api.handlers.ReactiveErrorHandler errorHelper) {
        this.ILoanUseCase = ILoanUseCase;
        this.logger = loggerFactoryPort.getLogger(LoanHandler.class);
        this.mapper = mapper;
        this.errorHelper = errorHelper;
    }

    public Mono<ServerResponse> crearteLoan(ServerRequest request) {

        return request.principal()
                .cast(Authentication.class)
                .flatMap(auth -> {
                    boolean autorizado = auth.getAuthorities().stream()
                            .anyMatch(a -> List.of("CLIENTE").contains(a.getAuthority()));
                    String token = request.headers().firstHeader(HttpHeaders.AUTHORIZATION);

                    if (!autorizado) {
                        return Mono.error(new AccessDeniedException("Acceso denegado"));
                    }
                    return request.bodyToMono(LoanRequest.class)
                            .doFirst(() -> logger.info("Iniciando creación de solicitud"))
                            .doOnNext(dto -> logger.info(String.format("Datos recibidos: %s", dto)))
                            .map(mapper::toEntity)
                            .flatMap(entity -> ILoanUseCase.createLoan(entity, token.replace("Bearer", "").trim()))
                            .map(mapper::toDto)
                            .flatMap(solicitudResponseDTO -> Mono.fromRunnable(() -> logger.info(String.format("Solicitud Creada %s", solicitudResponseDTO))).then(ServerResponse.ok().bodyValue(solicitudResponseDTO)))
                            .doOnTerminate(() -> logger.info("Flujo finalizado"))
                            .onErrorResume(errorHelper::handle);

                });
    }

    public Mono<ServerResponse> listarPorEstado(ServerRequest request) {
        return request.principal()
                .cast(Authentication.class)
                .flatMap(auth -> {
                    boolean autorizado = auth.getAuthorities().stream()
                            .anyMatch(a -> List.of("ASESOR").contains(a.getAuthority()));
                    String token = request.headers().firstHeader(HttpHeaders.AUTHORIZATION);

                    if (!autorizado) {
                        return Mono.error(new AccessDeniedException("Acceso denegado"));
                    }
                    String estado = request.queryParam("estado").orElse("");
                    int pagina = request.queryParam("page").map(Integer::parseInt).orElse(0);
                    int tamaño = request.queryParam("size").map(Integer::parseInt).orElse(10);

                    Flux<Loan> flujo = ILoanUseCase.listarSolicitudes(estado);
                    Mono<Long> total = flujo.count();

                    Mono<List<LoanResponse>> contenido = flujo
                            .skip((long) pagina * tamaño)
                            .take(tamaño)
                            .map(mapper::toDto)
                            .collectList();

                    return Mono.zip(contenido, total)
                            .map(tuple -> new PageableResponse<>(
                                    tuple.getT1(),
                                    pagina,
                                    tamaño,
                                    tuple.getT2(),
                                    (int) Math.ceil((double) tuple.getT2() / tamaño)
                            ))
                            .flatMap(ServerResponse.ok()::bodyValue)
                            .onErrorResume(errorHelper::handle);

                }).onErrorResume(errorHelper::handle);
    }


}
