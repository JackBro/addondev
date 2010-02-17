package org.addondev.rhino;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;


import org.mozilla.javascript.*;

public class JavaScriptSyntaxErrorReporter implements ErrorReporter {

	@Override
	public void error(String message, String sourceName, int line,
            String lineSource, int lineOffset) {
		// TODO Auto-generated method stub
		System.out.println("error : " + line +" : " +lineOffset + " : " +message);
        //reportErrorMessage(message, sourceName, line, lineSource, lineOffset, false);
	}

	@Override
	public EvaluatorException runtimeError(String message, String sourceName,
            int line, String lineSource,
            int lineOffset) {
		// TODO Auto-generated method stub
        return new EvaluatorException(message, sourceName, line,
                lineSource, lineOffset);
	}

	@Override
	public void warning(String message, String sourceName, int line,
            String lineSource, int lineOffset) {
		// TODO Auto-generated method stub
		System.out.println("warning : " + line +" : "+lineOffset + " : " +message);
	}
	
    public static String getMessage(String messageId) {
        return getMessage(messageId, (Object []) null);
    }

    public static String getMessage(String messageId, String argument) {
        Object[] args = { argument };
        return getMessage(messageId, args);
    }

    public static String getMessage(String messageId, Object arg1, Object arg2)
    {
        Object[] args = { arg1, arg2 };
        return getMessage(messageId, args);
    }

    public static String getMessage(String messageId, Object[] args) {
        Context cx = Context.getCurrentContext();
        Locale locale = cx == null ? Locale.getDefault() : cx.getLocale();

        // ResourceBundle does caching.
        ResourceBundle rb = ResourceBundle.getBundle
            ("org.mozilla.javascript.tools.resources.Messages", locale);

        String formatString;
        try {
            formatString = rb.getString(messageId);
        } catch (java.util.MissingResourceException mre) {
            throw new RuntimeException("no message resource found for message property "
                                       + messageId);
        }

        if (args == null) {
            return formatString;
        } else {
            MessageFormat formatter = new MessageFormat(formatString);
            return formatter.format(args);
        }
    }
	
	private void reportErrorMessage(String message, String sourceName,
			int line, String lineSource, int lineOffset, boolean justWarning) {
		if (line > 0) {
			String lineStr = String.valueOf(line);
			if (sourceName != null) {
				Object[] args = { sourceName, lineStr, message };
				message = getMessage("msg.format3", args);
			} else {
				Object[] args = { lineStr, message };
				message = getMessage("msg.format2", args);
			}
		} else {
			Object[] args = { message };
			message = getMessage("msg.format1", args);
		}
		if (justWarning) {
			message = getMessage("msg.warning", message);
		}
		System.out.println(message);
//		err.println(messagePrefix + message);
//		if (null != lineSource) {
//			err.println(messagePrefix + lineSource);
//			err.println(messagePrefix + buildIndicator(lineOffset));
//		}
	}

}
