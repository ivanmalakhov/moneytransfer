package com.revolute.handler;

import com.revolute.dto.Validable;

import java.util.Map;

public interface RequestHandler<V extends Validable> {

  Answer process(V value, Map<String, String> urlParams, boolean shouldReturnHtml);

}
