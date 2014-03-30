package com.github.pepe79.jats.view;

import java.util.Map;
import java.util.Set;

public interface ViewFactory
{
	Map<String, Map<Class<?>, Set<String>>> createViews();
}
