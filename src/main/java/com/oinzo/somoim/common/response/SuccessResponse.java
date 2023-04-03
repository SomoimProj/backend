package com.oinzo.somoim.common.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SuccessResponse<T> {
	protected static final SuccessResponse<?> NO_DATA_RESPONSE = new SuccessResponse<>();

	private boolean ok = true;
	private T data;

	protected SuccessResponse(T data) {
		this.data = data;
	}
}