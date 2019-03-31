package com.revolute.handler;

import java.util.Map;

public interface RequestHandler<V> {

  Answer process(V value, Map<String, String> urlParams);

}
