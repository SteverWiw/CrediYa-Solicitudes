package co.com.powerup2025.api.dtos.response;

import java.util.List;

public record PageableResponse <T> (List<T> content,
                                    int page,
                                    int size,
                                    long totalElements,
                                    int totalPages
) {}

