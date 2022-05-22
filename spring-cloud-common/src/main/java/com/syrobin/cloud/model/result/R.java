package com.syrobin.cloud.model.result;

import com.syrobin.cloud.enums.ResultCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 响应信息主体
 *
 * @param <T>
 * @author lengleng
 */

@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class R<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 响应码
	 */
	@Getter
	@Setter
	private int code;

	/**
	 * 响应消息
	 */
	@Getter
	@Setter
	private String msg;

	/**
	 * 响应数据
	 */
	@Getter
	@Setter
	private T data;

	public static <T> R<T> ok() {
		return restResult(null, ResultCodeEnum.SUCCESS.getCode(), null);
	}

	public static <T> R<T> ok(T data) {
		return restResult(data, ResultCodeEnum.SUCCESS.getCode(), null);
	}

	public static <T> R<T> ok(T data, String msg) {
		return restResult(data, ResultCodeEnum.SUCCESS.getCode(), msg);
	}

	public static <T> R<T> failed() {
		return restResult(null, ResultCodeEnum.FAIL.getCode(), null);
	}

	public static <T> R<T> failed(String msg) {
		return restResult(null, ResultCodeEnum.SUCCESS.getCode(), msg);
	}

	public static <T> R<T> failed(T data) {
		return restResult(data, ResultCodeEnum.SUCCESS.getCode(), null);
	}

	public static <T> R<T> failed(T data, String msg) {
		return restResult(data, ResultCodeEnum.SUCCESS.getCode(), msg);
	}

	private static <T> R<T> restResult(T data, int code, String msg) {
		R<T> apiResult = new R<>();
		apiResult.setCode(code);
		apiResult.setData(data);
		apiResult.setMsg(msg);
		return apiResult;
	}

}
