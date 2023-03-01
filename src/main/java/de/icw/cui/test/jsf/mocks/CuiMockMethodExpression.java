package de.icw.cui.test.jsf.mocks;

import java.io.Serializable;
import java.util.Arrays;

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.el.MethodInfo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Oliver Wolff
 */
@EqualsAndHashCode(callSuper = false)
@ToString
public class CuiMockMethodExpression extends MethodExpression {

    private static final long serialVersionUID = 2692860324272056192L;

    /** Indicates whether method was invoked. */
    @Getter
    @Setter
    private boolean invoked = false;

    /** Parameters method was invoked with. */
    @Getter
    private transient Object[] invokedParams;

    /** Result to be returned on {@link #invoke(ELContext, Object[])} */
    @Setter
    private Serializable invokeResult;

    @Setter
    private transient MethodInfo methodInfo;

    @Getter
    @Setter
    private String expressionString;

    @Getter
    @Setter
    private boolean literalText = false;

    @Override
    public MethodInfo getMethodInfo(final ELContext context) {
        return methodInfo;
    }

    @Override
    public Object invoke(final ELContext context, final Object[] params) {
        invoked = true;
        invokedParams = Arrays.copyOf(params, params.length);
        return invokeResult;
    }
}
