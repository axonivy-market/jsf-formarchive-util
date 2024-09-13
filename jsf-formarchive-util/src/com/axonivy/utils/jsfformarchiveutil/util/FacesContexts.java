package com.axonivy.utils.jsfformarchiveutil.util;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.ClassUtils;

public class FacesContexts {
	private FacesContexts() {
	}

	/**
	 * Invoke method of another bean from which bean called this method by method
	 * expression string</br>
	 * Exp: #{logic.plusTwoRealNumber}
	 *
	 * @param methodEL     method expression string
	 * @param parameters   array of parameter of method
	 * @param returnedType expected returned type of method calling
	 * @return return value
	 * @throws ELException        if method expression string is invalid
	 * @throws ClassCaseException if return type is wrong
	 */
	public static <E> E invokeMethodByExpression(String methodExpressionLiteral, Object[] parameters,
			Class<E> returnedType) {
		ELContext elContext = getELContext();
		Application application = getApplication();
		ExpressionFactory expressionFactory = application.getExpressionFactory();
		MethodExpression methodExpression = expressionFactory.createMethodExpression(elContext, methodExpressionLiteral,
				returnedType, ClassUtils.toClass(parameters));
		return invokeMethod(elContext, methodExpression, parameters, returnedType);
	}

	public static Application getApplication() {
		return getCurrentInstance().getApplication();
	}

	public static ELContext getELContext() {
		return getCurrentInstance().getELContext();
	}

	private static FacesContext getCurrentInstance() {
		return FacesContext.getCurrentInstance();
	}

	@SuppressWarnings("unchecked")
	private static <E> E invokeMethod(ELContext elContext, MethodExpression methodExpression, Object[] parameters,
			Class<E> returnedType) {
		try {
			Object result = methodExpression.invoke(elContext, parameters);
			return returnedType != null ? returnedType.cast(result) : (E) result;
		} catch (ELException e) {
			throw new RuntimeException("Cannot invoke method expression", e);
		} catch (ClassCastException ex) {
			throw new RuntimeException("The data type of return value is not as expected", ex);
		}
	}

}
