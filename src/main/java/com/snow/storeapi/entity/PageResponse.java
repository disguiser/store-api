package com.snow.storeapi.entity;

import java.util.List;

public record PageResponse(Integer total, List items) {
}
