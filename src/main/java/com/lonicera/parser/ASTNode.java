package com.lonicera.parser;


import com.lonicera.context.EvalContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public interface ASTNode {
    Object eval(EvalContext evalContext, Type targetType, Annotation[] targetAnnotations);
}
