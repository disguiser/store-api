package com.snow.storeapi.entity;

import java.util.List;

public record PageResponse(Object total, List<?> items) {
}
