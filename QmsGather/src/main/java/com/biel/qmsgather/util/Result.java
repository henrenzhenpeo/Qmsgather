package com.biel.qmsgather.util;


import com.biel.qmsgather.domain.AppearanceStatusCount;
import com.biel.qmsgather.domain.SizeStatusCount;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> {
	private int code;
	private String msg;
	private T data;
	private int count;
	private List<String> name;
	private SizeStatusCount statusCount;
	private AppearanceStatusCount appearanceStatusCount;
	private int pageCount;
	public Result(int code, String msg) {
		this(code, msg, null);
	}

	public Result(int code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	public Result(int code, String msg, SizeStatusCount statusCount, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
		this.statusCount=statusCount;
	}
	public Result(int code, String msg, AppearanceStatusCount appearanceStatusCount, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
		this.appearanceStatusCount=appearanceStatusCount;
	}
	public Result(int code, String msg, T data, int count) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
		this.count = count;
	}

	public Result(int code, String msg, T data, int count, int pageCount) {
		this.code = code;
		this.msg = msg;
		this.data = data;
		this.count = count;
		this.pageCount = pageCount;
	}

	public Result(int code, String msg, int count, T data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
		this.count = count;
	}

	public Result(int code, String msg, List<String> name, T data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
		this.name = name;
	}

	public Result(Errors errors) {
		StringBuilder msg = new StringBuilder();
		errors.getFieldErrors().forEach((ObjectError error) -> {
			msg.append(error.getDefaultMessage() + "\n");
		});

		this.code = 0;
		this.msg = msg.toString();
	}

	public final static Result INSERT_SUCCESS = new Result(200, "添加成功");
	public final static Result INSERT_FAILED = new Result(500, "添加失败");
	public final static Result DELETE_SUCCESS = new Result(200, "删除成功");
	public final static Result DELETE_FAILED = new Result(500, "删除失败");
	public final static Result UPDATE_SUCCESS = new Result(200, "更新成功");
	public final static Result UPDATE_FAILED = new Result(500, "更新失败");



}
