/*
 * Modified 2016 Sascha Münch
 * 
 * Copyright 2012 Udo Klimaschewski
 * 
 * http://UdoJava.com/
 * http://about.me/udo.klimaschewski
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 */
package de.sonnmatt.muutos.parser;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * <h1>EvalEx - Java Expression Evaluator</h1>
 * 
 * <h2>Introduction</h2> EvalEx is a handy expression evaluator for Java, that allows to evaluate simple mathematical, 
 * boolean, string and date expressions. <br>
 * Key Features:
 * <ul>
 * <li>Uses String for calculation and result (internally BigDecimal)</li>
 * <li>Single class implementation, very compact</li>
 * <li>No dependencies to external libraries</li>
 * <li>Precision and rounding mode can be set</li>
 * <li>Supports variables</li>
 * <li>Standard boolean and mathematical operators</li>
 * <li>Standard basic mathematical and boolean functions</li>
 * <li>Standard basic string and date functions</li>
 * <li>Custom functions and operators can be added at runtime</li>
 * </ul>
 * <br>
 * <h2>Examples</h2>
 * 
 * <pre>
 *  BigDecimal result = null;
 *  
 *  Expression expression = new Expression("1+1/3");
 *  result = expression.eval():
 *  expression.setPrecision(2);
 *  result = expression.eval():
 *  
 *  result = new Expression("(3.4 + -4.1)/2").eval();
 *  
 *  result = new Expression("SQRT(a^2 + b^2").with("a","2.4").and("b","9.253").eval();
 *  
 *  BigDecimal a = new BigDecimal("2.4");
 *  BigDecimal b = new BigDecimal("9.235");
 *  result = new Expression("SQRT(a^2 + b^2").with("a",a).and("b",b).eval();
 *  
 *  result = new Expression("2.4/PI").setPrecision(128).setRoundingMode(RoundingMode.UP).eval();
 *  
 *  result = new Expression("random() > 0.5").eval();
 * 
 *  result = new Expression("not(x<7 || sqrt(max(x,9)) <= 3))").with("x","22.9").eval();
 *  
 *  result = new Expression("LEFT(\"Text to manipulate\"", 4).eval();
 *  
 *  result = new Expression("YEAR(NOW())").eval();
 * </pre>
 * 
 * <br>
 * <h2>Supported Operators</h2>
 * <table>
 * <tr>
 * <th>Mathematical Operators</th>
 * </tr>
 * <tr>
 * <th>Operator</th>
 * <th>Description</th>
 * </tr>
 * <tr>
 * <td>+</td>
 * <td>Additive operator</td>
 * </tr>
 * <tr>
 * <td>-</td>
 * <td>Subtraction operator</td>
 * </tr>
 * <tr>
 * <td>*</td>
 * <td>Multiplication operator</td>
 * </tr>
 * <tr>
 * <td>/</td>
 * <td>Division operator</td>
 * </tr>
 * <tr>
 * <td>%</td>
 * <td>Remainder operator (Modulo)</td>
 * </tr>
 * <tr>
 * <td>^</td>
 * <td>Power operator</td>
 * </tr>
 * </table>
 * <br>
 * <table>
 * <tr>
 * <th>Boolean Operators<sup>*</sup></th>
 * </tr>
 * <tr>
 * <th>Operator</th>
 * <th>Description</th>
 * </tr>
 * <tr>
 * <td>=</td>
 * <td>Equals</td>
 * </tr>
 * <tr>
 * <td>==</td>
 * <td>Equals</td>
 * </tr>
 * <tr>
 * <td>!=</td>
 * <td>Not equals</td>
 * </tr>
 * <tr>
 * <td>&lt;&gt;</td>
 * <td>Not equals</td>
 * </tr>
 * <tr>
 * <td>&lt;</td>
 * <td>Less than</td>
 * </tr>
 * <tr>
 * <td>&lt;=</td>
 * <td>Less than or equal to</td>
 * </tr>
 * <tr>
 * <td>&gt;</td>
 * <td>Greater than</td>
 * </tr>
 * <tr>
 * <td>&gt;=</td>
 * <td>Greater than or equal to</td>
 * </tr>
 * <tr>
 * <td>&amp;&amp;</td>
 * <td>Boolean and</td>
 * </tr>
 * <tr>
 * <td>||</td>
 * <td>Boolean or</td>
 * </tr>
 * </table>
 * *Boolean operators result always in a BigDecimal value of 1 or 0 (zero). Any non-zero value is treated as a _true_
 * value. Boolean _not_ is implemented by a function. <br>
 * <h2>Supported Functions</h2>
 * <table>
 * <tr>
 * <th>Function<sup>*</sup></th>
 * <th>Description</th>
 * </tr>
 * <tr>
 * <td>NOT(<i>expression</i>)</td>
 * <td>Boolean negation, 1 (means true) if the expression is not zero</td>
 * </tr>
 * <tr>
 * <td>IF(<i>condition</i>,<i>value_if_true</i>,<i>value_if_false</i>)</td>
 * <td>Returns one value if the condition evaluates to true or the other if it evaluates to false</td>
 * </tr>
 * <tr>
 * <td>RANDOM()</td>
 * <td>Produces a random number between 0 and 1</td>
 * </tr>
 * <tr>
 * <td>MIN(<i>e1</i>,<i>e2</i>, <i>...</i>)</td>
 * <td>Returns the smallest of the given expressions</td>
 * </tr>
 * <tr>
 * <td>MAX(<i>e1</i>,<i>e2</i>, <i>...</i>)</td>
 * <td>Returns the biggest of the given expressions</td>
 * </tr>
 * <tr>
 * <td>ABS(<i>expression</i>)</td>
 * <td>Returns the absolute (non-negative) value of the expression</td>
 * </tr>
 * <tr>
 * <td>ROUND(<i>expression</i>,precision)</td>
 * <td>Rounds a value to a certain number of digits, uses the current rounding mode</td>
 * </tr>
 * <tr>
 * <td>FLOOR(<i>expression</i>)</td>
 * <td>Rounds the value down to the nearest integer</td>
 * </tr>
 * <tr>
 * <td>CEILING(<i>expression</i>)</td>
 * <td>Rounds the value up to the nearest integer</td>
 * </tr>
 * <tr>
 * <td>LOG(<i>expression</i>)</td>
 * <td>Returns the natural logarithm (base e) of an expression</td>
 * </tr>
 * <tr>
 * <td>LOG10(<i>expression</i>)</td>
 * <td>Returns the common logarithm (base 10) of an expression</td>
 * </tr>
 * <tr>
 * <td>SQRT(<i>expression</i>)</td>
 * <td>Returns the square root of an expression</td>
 * </tr>
 * <tr>
 * <td>SIN(<i>expression</i>)</td>
 * <td>Returns the trigonometric sine of an angle (in degrees)</td>
 * </tr>
 * <tr>
 * <td>COS(<i>expression</i>)</td>
 * <td>Returns the trigonometric cosine of an angle (in degrees)</td>
 * </tr>
 * <tr>
 * <td>TAN(<i>expression</i>)</td>
 * <td>Returns the trigonometric tangens of an angle (in degrees)</td>
 * </tr>
 * <tr>
 * <td>ASIN(<i>expression</i>)</td>
 * <td>Returns the angle of asin (in degrees)</td>
 * </tr>
 * <tr>
 * <td>ACOS(<i>expression</i>)</td>
 * <td>Returns the angle of acos (in degrees)</td>
 * </tr>
 * <tr>
 * <td>ATAN(<i>expression</i>)</td>
 * <td>Returns the angle of atan (in degrees)</td>
 * </tr>
 * <tr>
 * <td>ATAN2(<i>y</i>,<i>x</i>)</td>
 * <td>Returns the angle of atan2 (in degrees)</td>
 * </tr>
 * <tr>
 * <td>SINH(<i>expression</i>)</td>
 * <td>Returns the hyperbolic sine of a value</td>
 * </tr>
 * <tr>
 * <td>COSH(<i>expression</i>)</td>
 * <td>Returns the hyperbolic cosine of a value</td>
 * </tr>
 * <tr>
 * <td>TANH(<i>expression</i>)</td>
 * <td>Returns the hyperbolic tangens of a value</td>
 * </tr>
 * <tr>
 * <td>SEC(<i>expression</i>)</td>
 * <td>Returns the secant (in degrees)</td>
 * </tr>
 * <tr>
 * <td>CSC(<i>expression</i>)</td>
 * <td>Returns the cosecant (in degrees)</td>
 * </tr>
 * <tr>
 * <td>SECH(<i>expression</i>)</td>
 * <td>Returns the hyperbolic secant (in degrees)</td>
 * </tr>
 * <tr>
 * <td>CSCH(<i>expression</i>)</td>
 * <td>Returns the hyperbolic cosecant (in degrees)</td>
 * </tr>
 * <tr>
 * <td>COT(<i>expression</i>)</td>
 * <td>Returns the trigonometric cotangens of an angle (in degrees)</td>
 * </tr>
 * <tr>
 * <td>ACOT(<i>expression</i>)</td>
 * <td>Returns the angle of acot (in degrees)</td>
 * </tr>
 * <tr>
 * <td>COTH(<i>expression</i>)</td>
 * <td>Returns the hyperbolic cotangens of a value</td>
 * </tr>
 * <tr>
 * <td>ASINH(<i>expression</i>)</td>
 * <td>Returns the angle of hyperbolic sine (in degrees)</td>
 * </tr>
 * <tr>
 * <td>ACOSH(<i>expression</i>)</td>
 * <td>Returns the angle of hyperbolic cosine (in degrees)</td>
 * </tr>
 * <tr>
 * <td>ATANH(<i>expression</i>)</td>
 * <td>Returns the angle of hyperbolic tangens of a value</td>
 * </tr>
 * <td>RAD(<i>expression</i>)</td>
 * <td>Converts an angle measured in degrees to an approximately equivalent angle measured in radians</td>
 * </tr>
 * <tr>
 * <td>DEG(<i>expression</i>)</td>
 * <td>Converts an angle measured in radians to an approximately equivalent angle measured in degrees</td>
 * </tr>
 * <tr>
 * <td>LEFT(<i>expression</i>, <i>expression2</i>)</td>
 * <td>Left part of the string <i>expression</i> up to <i>expression2</i> characters</td>
 * </tr>
 * <tr>
 * <td>RIGHT(<i>expression</i>, <i>expression2</i>)</td>
 * <td>Right part of the string <i>expression</i> up to <i>expression2</i> characters</td>
 * </tr>
 * <tr>
 * <td>MID(<i>expression</i>, <i>expression2</i>, <i>expression3</i>)</td>
 * <td>Up to , <i>expression3</i> characters of the string <i>expression</i> starting from character number <i>expression2</i></td>
 * </tr>
 * <tr>
 * <td>INSTR(<i>expression</i>, <i>expression2</i>)</td>
 * <td>Returns TRUE im <i>expression2</i> is in <i>expression</i></td>
 * </tr>
 * <tr>
 * <td>LENGTH(<i>expression</i>)</td>
 * <td>Length of the string which results the <i>expression</i></td>
 * </tr>
 * <tr>
 * <td>CHR(<i>expression</i>)</td>
 * <td>Character from the ASCII value <i>expression</i></td>
 * </tr>
 * <tr>
 * <td>ASC(<i>expression</i>)</td>
 * <td>ASCII value from character <i>expression</i></td>
 * </tr>
 * <tr>
 * <td>TRIM(<i>expression</i>)</td>
 * <td>Removes leading and trailing whitespaces</td>
 * </tr>
 * <tr>
 * <td>UCASE(<i>expression</i>)</td>
 * <td>Converts <i>expression</i> in upper case</td>
 * </tr>
 * <tr>
 * <td>LCASE(<i>expression</i>)</td>
 * <td>Converts <i>expression</i> to lower case</td>
 * </tr>
 * <tr>
 * <td>REPLACE(<i>expression</i>, <i>expression2</i>, <i>expression3</i>)</td>
 * <td>Replaces in <i>expression</i> every <i>expression2</i> with <i>expression3</i></td>
 * </tr>
 * <tr>
 * <td>CSV(<i>expression</i>, <i>expression2</i>, <i>expression3</i>)</td>
 * <td>Splits <i>expression</i> at <i>expression2</i> and returns the <i>expression3</i> column</td>
 * </tr>
 * <tr>
 * <td>NOW(<i>expression</i>)</td>
 * <td><i>expression</i> can contain a different format for the return format of the current date and time</td>
 * </tr>
 * <tr>
 * <td>YEAR(<i>expression</i>)</td>
 * <td>Year of the date</td>
 * </tr>
 * <tr>
 * <td>Month(<i>expression</i>)</td>
 * <td>Month of the date</td>
 * </tr>
 * <tr>
 * <td>DAY(<i>expression</i>)</td>
 * <td>Day of month of the date</td>
 * </tr>
 * <tr>
 * <td>DATEPART(<i>expression</i>, <i>expression2</i>)</td>
 * <td>Returns <i>expression</i> from date <i>expression2</i>
 * <i>expression</i> can be:
 *	- YEAR
 *	- MONTH
 *	- DAY
 *	- HOUR
 *	- MINUTE
 *	- SECOND
 *	- MILLISECOND
 *	- WEEK
 *	- DAY_OF_YEAR
 *	- WEEKDAY 
 * </td>
 * </tr>
 * <tr>
 * <td>(<i>expression</i>)</td>
 * <td></td>
 * </tr>
 * </table>
 * *Functions names are case insensitive. <br>
 * <h2>Supported Constants</h2>
 * <table>
 * <tr>
 * <th>Constant</th>
 * <th>Description</th>
 * </tr>
 * <tr>
 * <td>e</td>
 * <td>The value of <i>e</i>, exact to 70 digits</td>
 * </tr>
 * <tr>
 * <td>PI</td>
 * <td>The value of <i>PI</i>, exact to 100 digits</td>
 * </tr>
 * <tr>
 * <td>TRUE</td>
 * <td>The value one</td>
 * </tr>
 * <tr>
 * <td>FALSE</td>
 * <td>The value zero</td>
 * </tr>
 * </table>
 * 
 * <h2>Add Custom Operators</h2>
 * 
 * Custom operators can be added easily, simply create an instance of `Expression.Operator` and add it to the
 * expression. Parameters are the operator string, its precedence and if it is left associative. The operators `eval()`
 * method will be called with the BigDecimal values of the operands. All existing operators can also be overridden. <br>
 * For example, add an operator `x >> n`, that moves the decimal point of _x_ _n_ digits to the right:
 * 
 * <pre>
 * Expression e = new Expression("2.1234 >> 2");
 * 
 * e.addOperator(e.new Operator(">>", 30, true) {
 *     {@literal @}Override
 *     public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
 *         return v1.movePointRight(v2.toBigInteger().intValue());
 *     }
 * });
 * 
 * e.eval(); // returns 212.34
 * </pre>
 * 
 * <br>
 * <h2>Add Custom Functions</h2>
 * 
 * Adding custom functions is as easy as adding custom operators. Create an instance of `Expression.Function`and add it
 * to the expression. Parameters are the function name and the count of required parameters. The functions `eval()`
 * method will be called with a list of the BigDecimal parameters. All existing functions can also be overridden. <br>
 * A <code>-1</code> as the number of parameters denotes a variable number of arguments.<br>
 * For example, add a function `average(a,b,c)`, that will calculate the average value of a, b and c: <br>
 * 
 * <pre>
 * Expression e = new Expression("2 * average(12,4,8)");
 * 
 * e.addFunction(e.new Function("average", 3) {
 *     {@literal @}Override
 *     public BigDecimal eval(List<BigDecimal> parameters) {
 *         BigDecimal sum = parameters.get(0).add(parameters.get(1)).add(parameters.get(2));
 *         return sum.divide(new BigDecimal(3));
 *     }
 * });
 * 
 * e.eval(); // returns 16
 * </pre>
 * 
 * The software is licensed under the MIT Open Source license (see LICENSE file). <br>
 * <ul>
 * <li>The *power of* operator (^) implementation was copied from [Stack Overflow
 * ](http://stackoverflow.com/questions/3579779/how-to-do-a-fractional-power -on-bigdecimal-in-java) Thanks to Gene
 * Marin</li>
 * <li>The SQRT() function implementation was taken from the book [The Java Programmers Guide To numerical
 * Computing](http://www.amazon.de/Java-Number-Cruncher -Programmers-Numerical/dp/0130460419) (Ronald Mak, 2002)</li>
 * </ul>
 * 
 * @author Udo Klimaschewski (http://about.me/udo.klimaschewski)
 * @author Sascha Münch
 */
public class Formula {

	static Logger log = LogManager.getLogger(Formula.class);

	/**
	 * Unary operators precedence: + and - as prefix
	 */
	public static final int OPERATOR_PRECEDENCE_UNARY = 60;

	/**
	 * Equality operators precedence: =, ==, !=. <>
	 */
	public static final int OPERATOR_PRECEDENCE_EQUALITY = 7;

	/**
	 * Comparative operators precedence: <,>,<=,>=
	 */
	public static final int OPERATOR_PRECEDENCE_COMPARISON = 10;

	/**
	 * Or operator precedence: ||
	 */
	public static final int OPERATOR_PRECEDENCE_OR = 2;

	/**
	 * And operator precedence: &&
	 */
	public static final int OPERATOR_PRECEDENCE_AND = 4;

	/**
	 * Power operator precedence: ^
	 */
	public static final int OPERATOR_PRECEDENCE_POWER = 40;

	/**
	 * Multiplicative operators precedence: *,/,%
	 */
	public static final int OPERATOR_PRECEDENCE_MULTIPLICATIVE = 30;

	/**
	 * Additive operators precedence: + and -
	 */
	public static final int OPERATOR_PRECEDENCE_ADDITIVE = 20;

	/**
	 * Definition of PI as a constant, can be used in expressions as variable.
	 */
	public static final String PI = new BigDecimal(
			"3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679").toString();

	/**
	 * Definition of e: "Euler's number" as a constant, can be used in expressions as variable.
	 */
	public static final String e = new BigDecimal("2.71828182845904523536028747135266249775724709369995957496696762772407663").toString();

	/**
	 * The {@link MathContext} to use for calculations.
	 */
	private MathContext mc = null;

	/**
	 * The characters (other than letters and digits) allowed as the first character in a variable.
	 */
	private String firstVarChars = "_[";

	/**
	 * The characters (other than letters and digits) allowed as the second or subsequent characters in a variable.
	 */
	private String varChars = "_";

	/**
	 * The original infix expression.
	 */
	private final String originalExpression;

	/**
	 * The current infix expression, with optional variable substitutions.
	 */
	private String expression = null;

	/**
	 * The cached RPN (Reverse Polish Notation) of the expression.
	 */
	private List<Token> rpn = null;

	/**
	 * All defined operators with name and implementation.
	 */
	private Map<String, Operator> operators = new TreeMap<String, Operator>(String.CASE_INSENSITIVE_ORDER);

	/**
	 * All defined functions with name and implementation.
	 */
	private Map<String, LazyFunction> functions = new TreeMap<String, LazyFunction>(String.CASE_INSENSITIVE_ORDER);

	/**
	 * All defined variables with name and value.
	 */
	private Map<String, String> variables = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);

	/**
	 * What character to use for decimal separators.
	 */
	private static final char decimalSeparator = '.';

	/**
	 * What character to use for minus sign (negative values).
	 */
	private static final char minusSign = '-';

	private static final String trueValue = "TRUE";
	private static final String falseValue = "FALSE";
	
	/**
	 * Default date/time format. 
	 */
	private String dateFormat = "dd.MM.yyyy HH.mm.ss";

	/**
	 * The BigDecimal representation of the left parenthesis, used for parsing varying numbers of function parameters.
	 */
	private static final LazyNumber PARAMS_START = new LazyNumber() {
		public String eval() {
			return null;
		}

		public String getString() {
			return null;
		}

		public BigDecimal getDecimal() {
			return null;
		}
	};

	
	/**
	 * The expression evaluators exception class.
	 */
	public class FormulaException extends RuntimeException {
		private static final long serialVersionUID = 1118142866870779047L;

		public FormulaException(String message) {
			super(message);
		}
	}

	/**
	 * LazyNumber interface created for lazily evaluated functions
	 */
	public interface LazyNumber {
		String eval();
		String getString();
		BigDecimal getDecimal();
	}
	
	public abstract class LazyFunction {
		/**
		 * Name of this function.
		 */
		private String name;
		/**
		 * Number of parameters expected for this function.
		 * <code>-1</code> denotes a variable number of parameters.
		 */
		private int numParams;
		
		/**
		 * Whether this function is a boolean function.
		 */
		protected boolean booleanFunction = false;

		/**
		 * Creates a new function with given name and parameter count.
		 *
		 * @param name
		 *            The name of the function.
		 * @param numParams
		 *            The number of parameters for this function.
		 *            <code>-1</code> denotes a variable number of parameters.
		 * @param booleanFunction
		 *            Whether this function is a boolean function.
		 */
		public LazyFunction(String name, int numParams, boolean booleanFunction) {
			this.name = name.toUpperCase(Locale.ROOT);
			this.numParams = numParams;
			this.booleanFunction = booleanFunction;
		}

		/**
		 * Creates a new function with given name and parameter count.
		 *
		 * @param name
		 *            The name of the function.
		 * @param numParams
		 *            The number of parameters for this function.
		 *            <code>-1</code> denotes a variable number of parameters.
		 */
		public LazyFunction(String name, int numParams) {
			this.name = name.toUpperCase(Locale.ROOT);
			this.numParams = numParams;
		}
		
		public String getName() {
			return name;
		}

		public int getNumParams() {
			return numParams;
		}

		public boolean numParamsVaries() {
			return numParams < 0;
		}
		
		public boolean isBooleanFunction() {
			return this.booleanFunction;
		}
		
		public void setBooleanFunction(boolean booleanFunction) {
			this.booleanFunction = booleanFunction;
		}
		
		public abstract LazyNumber lazyEval(List<LazyNumber> lazyParams);
	}

	/**
	 * Abstract definition of a supported expression function. A function is
	 * defined by a name, the number of parameters and the actual processing
	 * implementation.
	 */
	public abstract class Function extends LazyFunction {

		public Function(String name, int numParams) {
			super(name, numParams);
		}
		
		public Function(String name, int numParams, boolean booleanFunction) {
			super(name, numParams, booleanFunction);
		}

		public LazyNumber lazyEval(final List<LazyNumber> lazyParams) {
			return new LazyNumber() {
			    
			    private List<String> params;
			    
				public String eval() {
					return Function.this.eval(getParams());
				}

				public String getString() {
					return String.valueOf(Function.this.eval(getParams()));
				}
				
				private List<String> getParams() {
	                if (params == null) {
	                    params = new ArrayList<String>();
	                    for (LazyNumber lazyParam : lazyParams) {
	                        params.add(lazyParam.eval());
	                    }
	                }
		            return params;
				}

				@Override
				public BigDecimal getDecimal() {
					// TODO Auto-generated method stub
					return null;
				}
			};
		}

		/**
		 * Implementation for this function.
		 *
		 * @param parameters
		 *            Parameters will be passed by the expression evaluator as a
		 *            {@link List} of {@link BigDecimal} values.
		 * @return The function must return a new {@link BigDecimal} value as a
		 *         computing result.
		 */
		public abstract String eval(List<String> parameters);
	}
	
	/**
	 * Abstract definition of a supported operator. An operator is defined by
	 * its name (pattern), precedence and if it is left- or right associative.
	 */
	public abstract class Operator {
		/**
		 * This operators name (pattern).
		 */
		private String oper;
		/**
		 * Operators precedence.
		 */
		private int precedence;
		/**
		 * Operator is left associative.
		 */
		private boolean leftAssoc;
		/**
		 * Whether this operator is boolean or not.
		 */
		protected boolean booleanOperator = false;

		public boolean isBooleanOperator() {
			return booleanOperator;
		}

		public void setBooleanOperator(boolean booleanOperator) {
			this.booleanOperator = booleanOperator;
		}

		/**
		 * Creates a new operator.
		 * 
		 * @param oper
		 *            The operator name (pattern).
		 * @param precedence
		 *            The operators precedence.
		 * @param leftAssoc
		 *            <code>true</code> if the operator is left associative,
		 *            else <code>false</code>.
		 * @param booleanOperator
		 * 	          Whether this operator is boolean.
		 */
		public Operator(String oper, int precedence, boolean leftAssoc, boolean booleanOperator) {
			this.oper = oper;
			this.precedence = precedence;
			this.leftAssoc = leftAssoc;
			this.booleanOperator = booleanOperator;
		}
		
		/**
		 * Creates a new operator.
		 * 
		 * @param oper
		 *            The operator name (pattern).
		 * @param precedence
		 *            The operators precedence.
		 * @param leftAssoc
		 *            <code>true</code> if the operator is left associative,
		 *            else <code>false</code>.
		 */
		public Operator(String oper, int precedence, boolean leftAssoc) {
			this.oper = oper;
			this.precedence = precedence;
			this.leftAssoc = leftAssoc;
		}

		public String getOper() {
			return oper;
		}

		public int getPrecedence() {
			return precedence;
		}

		public boolean isLeftAssoc() {
			return leftAssoc;
		}

		/**
		 * Implementation for this operator.
		 * 
		 * @param v1
		 *            Operand 1.
		 * @param v2
		 *            Operand 2.
		 * @return The result of the operation.
		 */
		public abstract String eval(String v1, String v2);
	}
	
	public abstract class UnaryOperator extends Operator {

		public UnaryOperator(String oper, int precedence, boolean leftAssoc) {
			super(oper, precedence, leftAssoc);
		}

		@Override
		public String eval(String v1, String v2) {
			if(v2 != null) {
				throw new FormulaException("Did not expect a second parameter for unary operator");
			}
			return evalUnary(v1);
		}

		abstract public String evalUnary(String v1);
	}	

	enum TokenType {
		VARIABLE, FUNCTION, LITERAL, OPERATOR, UNARY_OPERATOR, OPEN_PAREN, COMMA, CLOSE_PAREN, HEX_LITERAL, STRINGPARAM
	}
	
	class Token {
		public String surface = "";
		public TokenType type;
		public int pos;

		public void append(char c) {
			surface += c;
		}

		public void append(String s) {
			surface += s;
		}

		public char charAt(int pos) {
			return surface.charAt(pos);
		}

		public int length() {
			return surface.length();
		}

		@Override
		public String toString() {
			return surface;
		}
	}
		
	
	/**
	 * Expression tokenizer that allows to iterate over a {@link String} expression token by token. Blank characters will be
	 * skipped.
	 */
	private class Tokenizer implements Iterator<Token> {

		/**
		 * Actual position in expression string.
		 */
		private int pos = 0;

		/**
		 * The original input expression.
		 */
		private String input;
		/**
		 * The previous token or <code>null</code> if none.
		 */
		private Token previousToken;

		/**
		 * Creates a new tokenizer for an expression.
		 * 
		 * @param input
		 *            The expression string.
		 */
		public Tokenizer(String input) {
			this.input = input.trim();
		}

		@Override
		public boolean hasNext() {
			return (pos < input.length());
		}

		/**
		 * Peek at the next character, without advancing the iterator.
		 * 
		 * @return The next character or character 0, if at end of string.
		 */
		private char peekNextChar() {
			if (pos < (input.length() - 1)) {
				return input.charAt(pos + 1);
			} else {
				return 0;
			}
		}

		private boolean isHexDigit(char ch) {
			return ch == 'x' || ch == 'X' || (ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'f') || (ch >= 'A' && ch <= 'F');
		}

		@Override
		public Token next() {
			Token token = new Token();

			if (pos >= input.length()) {
				return previousToken = null;
			}
			char ch = input.charAt(pos);
			while (Character.isWhitespace(ch) && pos < input.length()) {
				ch = input.charAt(++pos);
			}
			token.pos = pos;

			boolean isHex = false;

			if (Character.isDigit(ch) || (ch == decimalSeparator && Character.isDigit(peekNextChar()))) {
				if (ch == '0' && (peekNextChar() == 'x' || peekNextChar() == 'X'))
					isHex = true;
				while ((isHex && isHexDigit(ch)) || (Character.isDigit(ch) || ch == decimalSeparator || ch == 'e' || ch == 'E'
							|| (ch == minusSign && token.length() > 0
								&& ('e' == token.charAt(token.length() - 1) || 'E' == token.charAt(token.length() - 1)))
							|| (ch == '+' && token.length() > 0 
								&& ('e' == token.charAt(token.length() - 1) || 'E' == token.charAt(token.length() - 1)))
							) && (pos < input.length())) {
					token.append(input.charAt(pos++));
					ch = pos == input.length() ? 0 : input.charAt(pos);
				}
				token.type = isHex ? TokenType.HEX_LITERAL : TokenType.LITERAL;
			} else if (ch == '"') {
				pos++;
				if (previousToken.type != TokenType.STRINGPARAM) {
					ch = input.charAt(pos);
					while (ch != '"') {
						token.append(input.charAt(pos++));
						ch = pos == input.length() ? 0 : input.charAt(pos);
					}
					token.type = TokenType.STRINGPARAM;
				} else {
					return next();
				}
			} else if (Character.isLetter(ch) || firstVarChars.indexOf(ch) >= 0) {
				if (ch == '[') {
					while ((ch != ']') && (pos < input.length())) {
						token.append(input.charAt(pos++));
						ch = pos == input.length() ? 0 : input.charAt(pos);
					}
					if (ch == ']') {
						token.append(input.charAt(pos++));
						ch = pos == input.length() ? 0 : input.charAt(pos);						
					}
				} else {
					while ((Character.isLetter(ch) || Character.isDigit(ch) || varChars.indexOf(ch) >= 0
							|| token.length() == 0 && firstVarChars.indexOf(ch) >= 0) && (pos < input.length())) {
						token.append(input.charAt(pos++));
						ch = pos == input.length() ? 0 : input.charAt(pos);
					}
				}
				// Remove optional white spaces after function or variable name
				if (ch == ' ') {
					while (ch == ' ' && pos < input.length()) {
						ch = input.charAt(pos++);
					}
					pos--;
				}
				token.type = ch == '(' ? TokenType.FUNCTION : TokenType.VARIABLE;
			} else if (ch == '(' || ch == ')' || ch == ',') {
				if (ch == '(') {
					token.type = TokenType.OPEN_PAREN;
				} else if (ch == ')') {
					token.type = TokenType.CLOSE_PAREN;
				} else {
					token.type = TokenType.COMMA;
				}
				token.append(ch);
				pos++;
			} else {
				String greedyMatch = "";
				int initialPos = pos;
				ch = input.charAt(pos);
				int validOperatorSeenUntil = -1;
				while (!Character.isLetter(ch) && !Character.isDigit(ch) && firstVarChars.indexOf(ch) < 0 && !Character.isWhitespace(ch) && ch != '('
						&& ch != ')' && ch != ',' && (pos < input.length())) {
					greedyMatch += ch;
					pos++;
					if (operators.containsKey(greedyMatch)) {
						validOperatorSeenUntil = pos;
					}
					ch = pos == input.length() ? 0 : input.charAt(pos);
				}
				if (validOperatorSeenUntil != -1) {
					token.append(input.substring(initialPos, validOperatorSeenUntil));
					pos = validOperatorSeenUntil;
				} else {
					token.append(greedyMatch);
				}

				if (previousToken == null || previousToken.type == TokenType.OPERATOR || previousToken.type == TokenType.OPEN_PAREN
						|| previousToken.type == TokenType.COMMA) {
					token.surface += "u";
					token.type = TokenType.UNARY_OPERATOR;
				} else {
					token.type = TokenType.OPERATOR;
				}
			}
			return previousToken = token;
		}

		@Override
		public void remove() {
			throw new FormulaException("remove() not supported");
		}

	}

	/**
	 * Creates a new expression instance from an expression string with a given default match context of
	 * {@link MathContext#DECIMAL32}.
	 * 
	 * @param expression
	 *            The expression. E.g. <code>"2.4*sin(3)/(2-4)"</code> or <code>"sin(y)>0 & max(z, 3)>3"</code>
	 */
	public Formula(String expression) {
		this(expression, MathContext.DECIMAL32);
	}

	/**
	 * Creates a new expression instance from an expression string with a given default match context.
	 * 
	 * @param expression
	 *            The expression. E.g. <code>"2.4*sin(3)/(2-4)"</code> or <code>"sin(y)>0 & max(z, 3)>3"</code>
	 * @param defaultMathContext
	 *            The {@link MathContext} to use by default.
	 */
	public Formula(String expression, MathContext defaultMathContext) {
		this.mc = defaultMathContext;
		this.expression = expression;
		this.originalExpression = expression;
		addOperator(new Operator("+", OPERATOR_PRECEDENCE_ADDITIVE, true) {
			@Override
			public String eval(String v1, String v2) {
				assertNotNull(v1, v2);
				if (isNumber(v1) && isNumber(v2)) {
					return String.valueOf(toNumber(v1).add(toNumber(v2), mc));
				} else {
					return v1 + v2;
				}
			}
		});
		addOperator(new Operator("-", OPERATOR_PRECEDENCE_ADDITIVE, true) {
			@Override
			public String eval(String v1, String v2) {
				assertNotNull(v1, v2);
				if (isNumber(v1) && isNumber(v2)) {
					return String.valueOf(toNumber(v1).subtract(toNumber(v2), mc));
				} else {
					throw new ArithmeticException("Operands may not be string");
				}
			}
		});
		addOperator(new Operator("*", OPERATOR_PRECEDENCE_MULTIPLICATIVE, true) {
			@Override
			public String eval(String v1, String v2) {
				assertNotNull(v1, v2);
				if (isNumber(v1) && isNumber(v2)) {
					return String.valueOf(toNumber(v1).multiply(toNumber(v2), mc));
				} else {
					throw new ArithmeticException("Operands may not be string");
				}
			}
		});
		addOperator(new Operator("/", OPERATOR_PRECEDENCE_MULTIPLICATIVE, true) {
			@Override
			public String eval(String v1, String v2) {
				assertNotNull(v1, v2);
				if (isNumber(v1) && isNumber(v2)) {
					return String.valueOf(toNumber(v1).divide(toNumber(v2), mc));
				} else {
					throw new ArithmeticException("Operands may not be string");
				}
			}
		});
		addOperator(new Operator("%", OPERATOR_PRECEDENCE_MULTIPLICATIVE, true) {
			@Override
			public String eval(String v1, String v2) {
				assertNotNull(v1, v2);
				if (isNumber(v1) && isNumber(v2)) {
					return String.valueOf(toNumber(v1).remainder(toNumber(v2), mc));
				} else {
					throw new ArithmeticException("Operands may not be string");
				}
			}
		});
		addOperator(new Operator("^", OPERATOR_PRECEDENCE_POWER, false) {
			@Override
			public String eval(String v1, String v2) {
				assertNotNull(v1, v2);
				/*- 
				 * Thanks to Gene Marin:
				 * http://stackoverflow.com/questions/3579779/how-to-do-a-fractional-power-on-bigdecimal-in-java
				 */
				BigDecimal v2BD = toNumber(v2);
				int signOf2 = v2BD.signum();
				double dn1 = Double.valueOf(v1);
				v2BD = v2BD.multiply(new BigDecimal(signOf2)); // n2 is now positive
				BigDecimal remainderOf2 = v2BD.remainder(BigDecimal.ONE);
				BigDecimal n2IntPart = v2BD.subtract(remainderOf2);
				BigDecimal intPow = toNumber(v1).pow(n2IntPart.intValueExact(), mc);
				BigDecimal doublePow = new BigDecimal(Math.pow(dn1, remainderOf2.doubleValue()));

				BigDecimal result = intPow.multiply(doublePow, mc);
				if (signOf2 == -1) {
					result = BigDecimal.ONE.divide(result, mc.getPrecision(), RoundingMode.HALF_UP);
				}
				return result.toString();
			}
		});
		addOperator(new Operator("&&", OPERATOR_PRECEDENCE_AND, false, true) {
			@Override
			public String eval(String v1, String v2) {
				assertNotNull(v1, v2);
				boolean b1 = v1.compareTo(falseValue) != 0;
				boolean b2 = v2.compareTo(falseValue) != 0;
				return b1 && b2 ? trueValue : falseValue;
			}
		});
		addOperator(new Operator("||", OPERATOR_PRECEDENCE_OR, false, true) {
			@Override
			public String eval(String v1, String v2) {
				assertNotNull(v1, v2);
				boolean b1 = v1.compareTo(falseValue) != 0;
				boolean b2 = v2.compareTo(falseValue) != 0;
				return b1 || b2 ? trueValue : falseValue;
			}
		});

		addOperator(new Operator(">", OPERATOR_PRECEDENCE_COMPARISON, false, true) {
			@Override
			public String eval(String v1, String v2) {
				assertNotNull(v1, v2);
				return v1.compareTo(v2) == 1 ? trueValue : falseValue;
			}
		});
		addOperator(new Operator(">=", OPERATOR_PRECEDENCE_COMPARISON, false, true) {
			@Override
			public String eval(String v1, String v2) {
				assertNotNull(v1, v2);
				return v1.compareTo(v2) >= 0 ? trueValue : falseValue;
			}
		});
		addOperator(new Operator("<", OPERATOR_PRECEDENCE_COMPARISON, false, true) {
			@Override
			public String eval(String v1, String v2) {
				assertNotNull(v1, v2);
				return v1.compareTo(v2) == -1 ? trueValue : falseValue;
			}
		});
		addOperator(new Operator("<=", OPERATOR_PRECEDENCE_COMPARISON, false, true) {
			@Override
			public String eval(String v1, String v2) {
				assertNotNull(v1, v2);
				return v1.compareTo(v2) <= 0 ? trueValue : falseValue;
			}
		});
		addOperator(new Operator("=", OPERATOR_PRECEDENCE_EQUALITY, false, true) {
			@Override
			public String eval(String v1, String v2) {
				if (v1 == v2) {
					return trueValue;
				}
				if (v1 == null || v2 == null) {
					return falseValue;
				}
				return v1.compareTo(v2) == 0 ? trueValue : falseValue;
			}
		});
		addOperator(new Operator("==", OPERATOR_PRECEDENCE_EQUALITY, false, true) {
			@Override
			public String eval(String v1, String v2) {
				return operators.get("=").eval(v1, v2);
			}
		});
		addOperator(new Operator("!=", OPERATOR_PRECEDENCE_EQUALITY, false, true) {
			@Override
			public String eval(String v1, String v2) {
				if (v1 == v2) {
					return falseValue;
				}
				if (v1 == null || v2 == null) {
					return trueValue;
				}
				return v1.compareTo(v2) != 0 ? trueValue : falseValue;
			}
		});
		addOperator(new Operator("<>", OPERATOR_PRECEDENCE_EQUALITY, false, true) {
			@Override
			public String eval(String v1, String v2) {
				assertNotNull(v1, v2);
				return operators.get("!=").eval(v1, v2);
			}
		});

		addOperator(new UnaryOperator("-", OPERATOR_PRECEDENCE_UNARY, false) {
			@Override
			public String evalUnary(String v1) {
				assertNotNull(v1);
				if (isNumber(v1)) {
					return String.valueOf(toNumber(v1).multiply(toNumber("-1"), mc));
				} else {
					throw new ArithmeticException("Operand may not be string");
				}
			}
		});
		addOperator(new UnaryOperator("+", OPERATOR_PRECEDENCE_UNARY, false) {
			@Override
			public String evalUnary(String v1) {
				assertNotNull(v1);
				if (isNumber(v1)) {
					return String.valueOf(toNumber(v1).multiply(toNumber("1"), mc));
				} else {
					throw new ArithmeticException("Operand may not be string");
				}
			}
		});

		addFunction(new Function("NOT", 1, true) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0));
				boolean zero = parameters.get(0).compareTo(falseValue) == 0;
				return zero ? trueValue : falseValue;
			}
		});
		addLazyFunction(new LazyFunction("IF", 3) {
			@Override
			public LazyNumber lazyEval(List<LazyNumber> lazyParams) {
				String result = lazyParams.get(0).eval();
				assertNotNull(result);
				boolean isTrue = result.compareTo(falseValue) != 0;
				return isTrue ? lazyParams.get(1) : lazyParams.get(2);
			}
		});
		addFunction(new Function("RANDOM", 0) {
			@Override
			public String eval(List<String> parameters) {
				double d = Math.random();
				return (new BigDecimal(d, mc)).toString();
			}
		});
		addFunction(new Function("SIN", 1) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0));
				double d = Math.sin(Math.toRadians(Double.valueOf(parameters.get(0))));
				return (new BigDecimal(d, mc)).toString();
			}
		});
		addFunction(new Function("COS", 1) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0));
				double d = Math.cos(Math.toRadians(Double.valueOf(parameters.get(0))));
				return (new BigDecimal(d, mc)).toString();
			}
		});
		addFunction(new Function("TAN", 1) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0));
				double d = Math.tan(Math.toRadians(Double.valueOf(parameters.get(0))));
				return (new BigDecimal(d, mc)).toString();
			}
		});
		addFunction(new Function("ASIN", 1) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0));
				double d = Math.toDegrees(Math.asin(Double.valueOf(parameters.get(0))));
				return (new BigDecimal(d, mc)).toString();
			}
		});
		addFunction(new Function("ACOS", 1) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0));
				double d = Math.toDegrees(Math.acos(Double.valueOf(parameters.get(0))));
				return (new BigDecimal(d, mc)).toString();
			}
		});
		addFunction(new Function("ATAN", 1) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0));
				double d = Math.toDegrees(Math.atan(Double.valueOf(parameters.get(0))));
				return (new BigDecimal(d, mc)).toString();
			}
		});
		addFunction(new Function("ATAN2", 2) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0));
				double d = Math.toDegrees(Math.atan2(Double.valueOf(parameters.get(0)), Double.valueOf(parameters.get(1))));
				return (new BigDecimal(d, mc)).toString();
			}
		});
		addFunction(new Function("SINH", 1) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0));
				double d = Math.sinh(Double.valueOf(parameters.get(0)));
				return (new BigDecimal(d, mc)).toString();
			}
		});
		addFunction(new Function("COSH", 1) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0));
				double d = Math.cosh(Double.valueOf(parameters.get(0)));
				return (new BigDecimal(d, mc)).toString();
			}
		});
		addFunction(new Function("TANH", 1) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0));
				double d = Math.tanh(Double.valueOf(parameters.get(0)));
				return (new BigDecimal(d, mc)).toString();
			}
		});
		addFunction(new Function("SEC", 1) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0));
				/** Formula: sec(x) = 1 / cos(x) */
				double d = Math.cos(Math.toRadians(Double.valueOf(parameters.get(0))));
				double one = 1;
				return (new BigDecimal((one / d), mc)).toString();
			}
		});
		addFunction(new Function("CSC", 1) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0));
				/** Formula: csc(x) = 1 / sin(x) */
				double one = 1;
				double d = Math.sin(Math.toRadians(Double.valueOf(parameters.get(0))));
				return (new BigDecimal((one / d), mc)).toString();
			}
		});
		addFunction(new Function("SECH", 1) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0));
				/** Formula: sech(x) = 1 / cosh(x) */
				double one = 1;
				double d = Math.cosh(Double.valueOf(parameters.get(0)));
				return (new BigDecimal((one / d), mc)).toString();
			}
		});
		addFunction(new Function("CSCH", 1) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0));
				/** Formula: csch(x) = 1 / sinh(x) */
				double one = 1;
				double d = Math.sinh(Double.valueOf(parameters.get(0)));
				return (new BigDecimal((one / d), mc)).toString();
			}
		});
		addFunction(new Function("COT", 1) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0));
				/** Formula: cot(x) = cos(x) / sin(x) = 1 / tan(x) */
				double one = 1;
				double d = Math.tan(Math.toRadians(Double.valueOf(parameters.get(0))));
				return (new BigDecimal((one / d), mc)).toString();
			}
		});
		addFunction(new Function("ACOT", 1) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0));
				/** Formula: acot(x) = atan(1/x) */
				if (Double.valueOf(parameters.get(0)) == 0) {
					throw new FormulaException("Number must not be 0");
				}
				double d = Math.toDegrees(Math.atan(1 / Double.valueOf(parameters.get(0))));
				return (new BigDecimal(d, mc)).toString();
			}
		});
		addFunction(new Function("COTH", 1) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0));
				/** Formula: coth(x) = 1 / tanh(x) */
				double one = 1;
				double d = Math.tanh(Double.valueOf(parameters.get(0)));
				return (new BigDecimal((one / d), mc)).toString();
			}
		});
		addFunction(new Function("ASINH", 1) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0));
				/** Formula: asinh(x) = ln(x + sqrt(x^2 + 1)) */
				double d = Math.log(Double.valueOf(parameters.get(0)) + (Math.sqrt(Math.pow(Double.valueOf(parameters.get(0)), 2) + 1)));
				return (new BigDecimal(d, mc)).toString();
			}
		});
		addFunction(new Function("ACOSH", 1) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0));
				/** Formula: acosh(x) = ln(x + sqrt(x^2 - 1)) */
				if (Double.compare(Double.valueOf(parameters.get(0)), 1) < 0) {
					throw new FormulaException("Number must be x >= 1");
				}
				double d = Math.log(Double.valueOf(parameters.get(0)) + (Math.sqrt(Math.pow(Double.valueOf(parameters.get(0)), 2) - 1)));
				return (new BigDecimal(d, mc)).toString();
			}
		});
		addFunction(new Function("ATANH", 1) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0));
				/** Formula: atanh(x) = 0.5*ln((1 + x)/(1 - x)) */
				if (Math.abs(Double.valueOf(parameters.get(0))) > 1 || Math.abs(Double.valueOf(parameters.get(0))) == 1) {
					throw new FormulaException("Number must be |x| < 1");
				}
				double d = 0.5 * Math.log((1 + Double.valueOf(parameters.get(0))) / (1 - Double.valueOf(parameters.get(0))));
				return (new BigDecimal(d, mc)).toString();
			}
		});
		addFunction(new Function("RAD", 1) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0));
				double d = Math.toRadians(Double.valueOf(parameters.get(0)));
				return (new BigDecimal(d, mc)).toString();
			}
		});
		addFunction(new Function("DEG", 1) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0));
				double d = Math.toDegrees(Double.valueOf(parameters.get(0)));
				return (new BigDecimal(d, mc)).toString();
			}
		});
		addFunction(new Function("MAX", -1) {
			@Override
			public String eval(List<String> parameters) {
				if (parameters.size() == 0) {
					throw new FormulaException("MAX requires at least one parameter");
				}
				String max = null;
				for (String parameter : parameters) {
					assertNotNull(parameter);
					if (max == null || parameter.compareTo(max) > 0) {
						max = parameter;
					}
				}
				return max;
			}
		});
		addFunction(new Function("MIN", -1) {
			@Override
			public String eval(List<String> parameters) {
				if (parameters.size() == 0) {
					throw new FormulaException("MIN requires at least one parameter");
				}
				String min = null;
				for (String parameter : parameters) {
					assertNotNull(parameter);
					if (min == null || parameter.compareTo(min) < 0) {
						min = parameter;
					}
				}
				return min;
			}
		});
		addFunction(new Function("ABS", 1) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0));
				return toNumber(parameters.get(0)).abs(mc).toString();
			}
		});
		addFunction(new Function("LOG", 1) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0));
				double d = Math.log(Double.valueOf(parameters.get(0)));
				return (new BigDecimal(d, mc)).toString();
			}
		});
		addFunction(new Function("LOG10", 1) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0));
				double d = Math.log10(Double.valueOf(parameters.get(0)));
				return (new BigDecimal(d, mc)).toString();
			}
		});
		addFunction(new Function("ROUND", 2) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0), parameters.get(1));
				BigDecimal toRound = toNumber(parameters.get(0));
				int precision = toNumber(parameters.get(1)).intValue();
				return toRound.setScale(precision, mc.getRoundingMode()).toString();
			}
		});
		addFunction(new Function("FLOOR", 1) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0));
				BigDecimal toRound = toNumber(parameters.get(0));
				return toRound.setScale(0, RoundingMode.FLOOR).toString();
			}
		});
		addFunction(new Function("CEILING", 1) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0));
				BigDecimal toRound = toNumber(parameters.get(0));
				return toRound.setScale(0, RoundingMode.CEILING).toString();
			}
		});
		addFunction(new Function("SQRT", 1) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0));
				/*
				 * From The Java Programmers Guide To numerical Computing (Ronald Mak, 2003)
				 */
				BigDecimal x = toNumber(parameters.get(0));
				if (x.compareTo(BigDecimal.ZERO) == 0) {
					return (new BigDecimal(0)).toString();
				}
				if (x.signum() < 0) {
					throw new FormulaException("Argument to SQRT() function must not be negative");
				}
				BigInteger n = x.movePointRight(mc.getPrecision() << 1).toBigInteger();

				int bits = (n.bitLength() + 1) >> 1;
				BigInteger ix = n.shiftRight(bits);
				BigInteger ixPrev;

				do {
					ixPrev = ix;
					ix = ix.add(n.divide(ix)).shiftRight(1);
					// Give other threads a chance to work;
					Thread.yield();
				} while (ix.compareTo(ixPrev) != 0);

				return (new BigDecimal(ix, mc.getPrecision())).toString();
			}
		});

		addFunction(new Function("LEFT", 2) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0), parameters.get(1));
				String v1 = parameters.get(0);
				int v2 = Integer.valueOf(parameters.get(1)).intValue();
				return v1.substring(0, v2);
			}
		});
		addFunction(new Function("RIGHT", 2) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0), parameters.get(1));
				String v1 = parameters.get(0);
				int v2 = Integer.valueOf(parameters.get(1)).intValue();
				return v1.substring(v1.length()-v2, v2);
			}
		});
		addFunction(new Function("MID", 3) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0), parameters.get(1));
				String v1 = parameters.get(0);
				int v2 = Integer.valueOf(parameters.get(1)).intValue();
				int v3 = Integer.valueOf(parameters.get(2)).intValue();
				return v1.substring(v2, v2+v3);
			}
		});
		addFunction(new Function("INSTR", 2) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0), parameters.get(1));
				String v1 = parameters.get(0);
				String v2 = parameters.get(1);
				return String.valueOf(v1.indexOf(v2));
			}
		});
		addFunction(new Function("LENGTH", 1) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0));
				String v1 = parameters.get(0);
				return String.valueOf(v1.length());
			}
		});
		addFunction(new Function("CHR", 1) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0));
				int v1 = Integer.valueOf(parameters.get(0)).intValue();
				return String.valueOf(Character.toChars(v1));
			}
		});
		addFunction(new Function("ASC", 1) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0));
				String v1 = parameters.get(0);
				return Integer.valueOf(v1).toString();
			}
		});
		addFunction(new Function("TRIM", 1) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0));
				return parameters.get(0).trim();
			}
		});
		addFunction(new Function("UCASE", 1) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0));
				return parameters.get(0).toUpperCase();
			}
		});
		addFunction(new Function("LCASE", 1) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0));
				return parameters.get(0).toLowerCase();
			}
		});
		addFunction(new Function("REPLACE", 3) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0), parameters.get(1));
				String v1 = parameters.get(0);
				String v2 = parameters.get(1);
				String v3 = parameters.get(2);
				return v1.replace(v2, v3);
			}
		});
		addFunction(new Function("CSV", 3) {
			@Override
			public String eval(List<String> parameters) {
				// Text, Delimeter, Column
				assertNotNull(parameters.get(0), parameters.get(1));
				String v1 = parameters.get(0);
				String v2 = parameters.get(1);
				int v3 = Integer.valueOf(parameters.get(2)).intValue();
				return v1.split(v2, v3+1)[v3];
			}
		});
		addFunction(new Function("NOW", -1) {
			@Override
			public String eval(List<String> parameters) {
				String usedFormat = parameters.size() == 0 ? dateFormat : parameters.get(0);
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern(usedFormat);
				return LocalDateTime.now().format(dtf);
			}
		});
		addFunction(new Function("DATEPART", 2) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0),parameters.get(2));
				return datePart(parameters.get(0), parameters.get(1));
			}
		});
		addFunction(new Function("YEAR", 1) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0));
				return datePart("YEAR", parameters.get(0));
			}
		});
		addFunction(new Function("MONTH", 1) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0));
				return datePart("MONTH", parameters.get(0));
			}
		});
		addFunction(new Function("DAY", 1) {
			@Override
			public String eval(List<String> parameters) {
				assertNotNull(parameters.get(0));
				return datePart("DAY", parameters.get(0));
			}
		});

		
		variables.put("e", e);
		variables.put("PI", PI);
		variables.put("NULL", null);
		variables.put("TRUE", Boolean.TRUE.toString());
		variables.put("FALSE", Boolean.FALSE.toString());

	}

	private String datePart(String v1, String v2) {
		String usedFormat = dateFormat.length() > v2.length() ? dateFormat.substring(0, v2.length()) : dateFormat;
		DateTimeFormatter dtf = new DateTimeFormatterBuilder()
								        .appendPattern(usedFormat)
								        .optionalStart()
								        .appendPattern(" HH:mm")
								        .optionalEnd()
								        .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
								        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
								        .toFormatter();
		
		LocalDateTime ld = LocalDateTime.parse(v2, dtf);
		switch (v1) {
		case "YEAR":
			return String.valueOf(ld.getYear());
		case "MONTH":
			return String.valueOf(ld.getMonthValue());
		case "DAY":
			return String.valueOf(ld.getDayOfMonth());
		case "HOUR":
			return String.valueOf(ld.getHour());
		case "MINUTE":
			return String.valueOf(ld.getMinute());
		case "SECOND":
			return String.valueOf(ld.getSecond());
		case "MILLISECOND":
			return String.valueOf(ld.getNano());
		case "WEEK":
			return String.valueOf(ld.get(WeekFields.ISO.weekOfYear()));
		case "DAY_OF_YEAR":
			return String.valueOf(ld.getDayOfYear());
		case "WEEKDAY":
			return String.valueOf(ld.getDayOfWeek());
		default:
			return null;
		}
    }

	private void assertNotNull(String v1) {
		if (v1 == null) {
			throw new ArithmeticException("Operand may not be null");
		}
	}

	private void assertNotNull(String v1, String v2) {
		if (v1 == null) {
			throw new ArithmeticException("First operand may not be null");
		}
		if (v2 == null) {
			throw new ArithmeticException("Second operand may not be null");
		}
	}

	/**
	 * Is the string a number?
	 * 
	 * @param st
	 *            The string.
	 * @return <code>true</code>, if the input string is a number.
	 */
	private boolean isNumber(String st) {
		if (st.charAt(0) == minusSign && st.length() == 1)
			return false;
		if (st.charAt(0) == '+' && st.length() == 1)
			return false;
		if (st.charAt(0) == decimalSeparator && (st.length() == 1 || !Character.isDigit(st.charAt(1))))
			return false;
		if (st.charAt(0) == 'e' || st.charAt(0) == 'E')
			return false;
		for (char ch : st.toCharArray()) {
			if (!Character.isDigit(ch) && ch != minusSign && ch != decimalSeparator && ch != 'e' && ch != 'E' && ch != '+')
				return false;
		}
		return true;
	}

	/**
	 * Generate number from string
	 * 
	 * @param st
	 *            The string.
	 * @return value of string as BigDecimal
	 */
	private BigDecimal toNumber(String st) {
		return BigDecimal.valueOf(Double.valueOf(st));
	}

	/**
	 * Implementation of the <i>Shunting Yard</i> algorithm to transform an infix expression to a RPN expression.
	 * 
	 * @param expression
	 *            The input expression in infx.
	 * @return A RPN representation of the expression, with each token as a list member.
	 */
	private List<Token> shuntingYard(String expression) {
		List<Token> outputQueue = new ArrayList<Token>();
		Stack<Token> stack = new Stack<Token>();

		Tokenizer tokenizer = new Tokenizer(expression);

		Token lastFunction = null;
		Token previousToken = null;
		while (tokenizer.hasNext()) {
			Token token = tokenizer.next();
			switch (token.type) {
			case STRINGPARAM:
				stack.push(token);
				break;
			case LITERAL:
			case HEX_LITERAL:
				outputQueue.add(token);
				break;
			case VARIABLE:
				outputQueue.add(token);
				break;
			case FUNCTION:
				stack.push(token);
				lastFunction = token;
				break;
			case COMMA:
				if (previousToken != null && previousToken.type == TokenType.OPERATOR) {
					throw new FormulaException("Missing parameter(s) for operator " + previousToken + " at character position " + previousToken.pos);
				}
				while (!stack.isEmpty() && stack.peek().type != TokenType.OPEN_PAREN) {
					outputQueue.add(stack.pop());
				}
				if (stack.isEmpty()) {
					throw new FormulaException("Parse error for function '" + lastFunction + "'");
				}
				break;
			case OPERATOR: {
				if (previousToken != null && (previousToken.type == TokenType.COMMA || previousToken.type == TokenType.OPEN_PAREN)) {
					throw new FormulaException("Missing parameter(s) for operator " + token + " at character position " + token.pos);
				}
				Operator o1 = operators.get(token.surface);
				if (o1 == null) {
					throw new FormulaException("Unknown operator '" + token + "' at position " + (token.pos + 1));
				}

				shuntOperators(outputQueue, stack, o1);
				stack.push(token);
				break;
			}
			case UNARY_OPERATOR: {
				if (previousToken != null && previousToken.type != TokenType.OPERATOR && previousToken.type != TokenType.COMMA
						&& previousToken.type != TokenType.OPEN_PAREN) {
					throw new FormulaException("Invalid position for unary operator " + token + " at character position " + token.pos);
				}
				Operator o1 = operators.get(token.surface);
				if (o1 == null) {
					throw new FormulaException(
							"Unknown unary operator '" + token.surface.substring(0, token.surface.length() - 1) + "' at position " + (token.pos + 1));
				}

				shuntOperators(outputQueue, stack, o1);
				stack.push(token);
				break;
			}
			case OPEN_PAREN:
				if (previousToken != null) {
					if (previousToken.type == TokenType.LITERAL || previousToken.type == TokenType.CLOSE_PAREN
							|| previousToken.type == TokenType.VARIABLE || previousToken.type == TokenType.HEX_LITERAL) {
						// Implicit multiplication, e.g. 23(a+b) or (a+b)(a-b)
						Token multiplication = new Token();
						multiplication.append("*");
						multiplication.type = TokenType.OPERATOR;
						stack.push(multiplication);
					}
					// if the ( is preceded by a valid function, then it
					// denotes the start of a parameter list
					if (previousToken.type == TokenType.FUNCTION) {
						outputQueue.add(token);
					}
				}
				stack.push(token);
				break;
			case CLOSE_PAREN:
				if (previousToken != null && previousToken.type == TokenType.OPERATOR) {
					throw new FormulaException("Missing parameter(s) for operator " + previousToken + " at character position " + previousToken.pos);
				}
				while (!stack.isEmpty() && stack.peek().type != TokenType.OPEN_PAREN) {
					outputQueue.add(stack.pop());
				}
				if (stack.isEmpty()) {
					throw new FormulaException("Mismatched parentheses");
				}
				stack.pop();
				if (!stack.isEmpty() && stack.peek().type == TokenType.FUNCTION) {
					outputQueue.add(stack.pop());
				}
			}
			previousToken = token;
		}

		while (!stack.isEmpty()) {
			Token element = stack.pop();
			if (element.type == TokenType.OPEN_PAREN || element.type == TokenType.CLOSE_PAREN) {
				throw new FormulaException("Mismatched parentheses");
			}
			outputQueue.add(element);
		}
		return outputQueue;
	}

	private void shuntOperators(List<Token> outputQueue, Stack<Token> stack, Operator o1) {
		Token nextToken = stack.isEmpty() ? null : stack.peek();
		while (nextToken != null && (nextToken.type == TokenType.OPERATOR || nextToken.type == TokenType.UNARY_OPERATOR)
				&& ((o1.isLeftAssoc() && o1.getPrecedence() <= operators.get(nextToken.surface).getPrecedence())
						|| (o1.getPrecedence() < operators.get(nextToken.surface).getPrecedence()))) {
			outputQueue.add(stack.pop());
			nextToken = stack.isEmpty() ? null : stack.peek();
		}
	}

	/**
	 * Evaluates the expression.
	 * 
	 * @return The result of the expression. Trailing zeros are stripped.
	 */
	public String eval() {
		return eval(true);
	}

	/**
	 * Evaluates the expression.
	 * 
	 * @param stripTrailingZeros
	 *            If set to <code>true</code> trailing zeros in the result are stripped.
	 * 
	 * @return The result of the expression.
	 */
	public String eval(boolean stripTrailingZeros) {

		Stack<LazyNumber> stack = new Stack<LazyNumber>();

		for (final Token token : getRPN()) {
			switch (token.type) {
			case UNARY_OPERATOR: {
				final LazyNumber value = stack.pop();
				LazyNumber result = new LazyNumber() {
					public String eval() {
						return operators.get(token.surface).eval(value.eval(), null);
					}

					@Override
					public String getString() {
						return operators.get(token.surface).eval(value.eval(), null);
					}

					@Override
					public BigDecimal getDecimal() {
						return toNumber(operators.get(token.surface).eval(value.eval(), null));
					}
				};
				stack.push(result);
				break;
			}
			case OPERATOR:
				final LazyNumber v1 = stack.pop();
				final LazyNumber v2 = stack.pop();
				LazyNumber result = new LazyNumber() {
					public String eval() {
						return operators.get(token.surface).eval(v2.eval(), v1.eval());
					}

					public String getString() {
						return operators.get(token.surface).eval(v2.eval(), v1.eval());
					}

					@Override
					public BigDecimal getDecimal() {
						return toNumber(operators.get(token.surface).eval(v2.eval(), v1.eval()));
					}
				};
				stack.push(result);
				break;
			case VARIABLE:
				if (!variables.containsKey(token.surface)) {
					throw new FormulaException("Unknown operator or function: " + token);
				}

				stack.push(new LazyNumber() {
					public String eval() {
						String value = variables.get(token.surface);
						//return value == null ? null : toNumber(value).round(mc).toString();
						return value == null ? null : value;
					}

					public String getString() {
						return token.surface;
					}

					@Override
					public BigDecimal getDecimal() {
						return toNumber(token.surface);
					}
				});
				break;
			case FUNCTION:
				LazyFunction f = functions.get(token.surface.toUpperCase(Locale.ROOT));
				ArrayList<LazyNumber> p = new ArrayList<LazyNumber>(!f.numParamsVaries() ? f.getNumParams() : 0);
				// pop parameters off the stack until we hit the start of
				// this function's parameter list
				while (!stack.isEmpty() && stack.peek() != PARAMS_START) {
					p.add(0, stack.pop());
				}

				if (stack.peek() == PARAMS_START) {
					stack.pop();
				}

				LazyNumber fResult = f.lazyEval(p);
				stack.push(fResult);
				break;
			case OPEN_PAREN:
				stack.push(PARAMS_START);
				break;
			case LITERAL:
				stack.push(new LazyNumber() {
					public String eval() {
						if (token.surface.equalsIgnoreCase("NULL")) {
							return null;
						}
						return (new BigDecimal(token.surface, mc)).toString();
					}

					public String getString() {
						return String.valueOf(new BigDecimal(token.surface, mc));
					}

					@Override
					public BigDecimal getDecimal() {
						return new BigDecimal(token.surface, mc);
					}
				});
				break;
			case STRINGPARAM:
				stack.push(new LazyNumber() {
					public String eval() {
						//return null;
						return token.surface;
					}

					public String getString() {
						return token.surface;
					}

					@Override
					public BigDecimal getDecimal() {
						return new BigDecimal(token.surface, mc);
					}
				});
				break;
			case HEX_LITERAL:
				stack.push(new LazyNumber() {
					public String eval() {
						return (new BigDecimal(new BigInteger(token.surface.substring(2), 16), mc)).toString();
					}

					public String getString() {
						return new BigInteger(token.surface.substring(2), 16).toString();
					}

					@Override
					public BigDecimal getDecimal() {
						return new BigDecimal(new BigInteger(token.surface.substring(2), 16), mc);
					}
				});
				break;
			case CLOSE_PAREN:
				break;
			case COMMA:
				break;
			default:
				break;
			}
		}
		String result = stack.pop().eval();
		if (result == null) {
			return null;
		} else if (isNumber(result)) {
			return stripTrailingZeros ? toNumber(result).stripTrailingZeros().toString() : result;
		} else {
			return result;
		}
		//return result == null ? null : stripTrailingZeros ? toNumber(result).stripTrailingZeros().toString() : result;
	}

	/**
	 * Sets the precision for expression evaluation.
	 * 
	 * @param precision
	 *            The new precision.
	 * 
	 * @return The expression, allows to chain methods.
	 */
	public Formula setPrecision(int precision) {
		this.mc = new MathContext(precision);
		return this;
	}

	/**
	 * Sets the rounding mode for expression evaluation.
	 * 
	 * @param roundingMode
	 *            The new rounding mode.
	 * @return The expression, allows to chain methods.
	 */
	public Formula setRoundingMode(RoundingMode roundingMode) {
		this.mc = new MathContext(mc.getPrecision(), roundingMode);
		return this;
	}

	/**
	 * Sets the characters other than letters and digits that are valid as the first character of a variable.
	 *
	 * @param chars
	 *            The new set of variable characters.
	 * @return The expression, allows to chain methods.
	 */
	public Formula setFirstVariableCharacters(String chars) {
		this.firstVarChars = chars;
		return this;
	}

	/**
	 * Sets the date/time format which will be used by date relevant functions as the default format.
	 *
	 * @param dateTimeFormat
	 *            The new set of of format.
	 * @return The expression, allows to chain methods.
	 */
	public Formula setDateFormat(String dateTimeFormat) {
		this.dateFormat = dateTimeFormat;
		return this;
	}

	/**
	 * Sets the characters other than letters and digits that are valid as the second and subsequent characters of a
	 * variable.
	 *
	 * @param chars
	 *            The new set of variable characters.
	 * @return The expression, allows to chain methods.
	 */
	public Formula setVariableCharacters(String chars) {
		this.varChars = chars;
		return this;
	}

	/**
	 * Adds an operator to the list of supported operators.
	 * 
	 * @param operator
	 *            The operator to add.
	 * @return The previous operator with that name, or <code>null</code> if there was none.
	 */
	public Operator addOperator(Operator operator) {
		String key = operator.getOper();
		if (operator instanceof UnaryOperator) {
			key += "u";
		}
		return operators.put(key, operator);
	}

	/**
	 * Adds a function to the list of supported functions
	 * 
	 * @param function
	 *            The function to add.
	 * @return The previous operator with that name, or <code>null</code> if there was none.
	 */
	public Function addFunction(Function function) {
		return (Function) functions.put(function.getName(), function);
	}

	/**
	 * Adds a lazy function function to the list of supported functions
	 *
	 * @param function
	 *            The function to add.
	 * @return The previous operator with that name, or <code>null</code> if there was none.
	 */
	public LazyFunction addLazyFunction(LazyFunction function) {
		return functions.put(function.getName(), function);
	}

	/**
	 * Sets a variable value.
	 * 
	 * @param variable
	 *            The variable name.
	 * @param value
	 *            The variable value.
	 * @return The expression, allows to chain methods.
	 */
	public Formula setVariable(String variable, BigDecimal value) {
		variables.put(variable, value.toString());
		return this;
	}

	/**
	 * Sets a variable value.
	 * 
	 * @param variable
	 *            The variable to set.
	 * @param value
	 *            The variable value.
	 * @return The expression, allows to chain methods.
	 */
	public Formula setVariable(String variable, String value) {
		if (!isNumber(value))
			variables.put(variable, value);
		else if (value.equalsIgnoreCase("null")) {
			variables.put(variable, null);
		} else {
			expression = expression.replaceAll("(?i)\\b" + variable + "\\b", "(" + value + ")");
			rpn = null;
		}
		return this;
	}

	/**
	 * Sets a variable value.
	 * 
	 * @param variable
	 *            The variable to set.
	 * @param value
	 *            The variable value.
	 * @return The expression, allows to chain methods.
	 */
	public Formula with(String variable, BigDecimal value) {
		return setVariable(variable, value);
	}

	/**
	 * Sets a variable value.
	 * 
	 * @param variable
	 *            The variable to set.
	 * @param value
	 *            The variable value.
	 * @return The expression, allows to chain methods.
	 */
	public Formula and(String variable, String value) {
		return setVariable(variable, value);
	}

	/**
	 * Sets a variable value.
	 * 
	 * @param variable
	 *            The variable to set.
	 * @param value
	 *            The variable value.
	 * @return The expression, allows to chain methods.
	 */
	public Formula and(String variable, BigDecimal value) {
		return setVariable(variable, value);
	}

	/**
	 * Sets a variable value.
	 * 
	 * @param variable
	 *            The variable to set.
	 * @param value
	 *            The variable value.
	 * @return The expression, allows to chain methods.
	 */
	public Formula with(String variable, String value) {
		return setVariable(variable, value);
	}

	/**
	 * Get an iterator for this expression, allows iterating over an expression token by token.
	 * 
	 * @return A new iterator instance for this expression.
	 */
	public Iterator<Token> getExpressionTokenizer() {
		final String expression = this.expression;

		return new Tokenizer(expression);
	}

	/**
	 * Cached access to the RPN notation of this expression, ensures only one calculation of the RPN per expression
	 * instance. If no cached instance exists, a new one will be created and put to the cache.
	 * 
	 * @return The cached RPN instance.
	 */
	private List<Token> getRPN() {
		if (rpn == null) {
			rpn = shuntingYard(this.expression);
			validate(rpn);
		}
		return rpn;
	}

	/**
	 * Check that the expression has enough numbers and variables to fit the requirements of the operators and functions,
	 * also check for only 1 result stored at the end of the evaluation.
	 */
	private void validate(List<Token> rpn) {
		/*-
		* Thanks to Norman Ramsey:
		* http://http://stackoverflow.com/questions/789847/postfix-notation-validation
		*/
		// each push on to this stack is a new function scope, with the value of each
		// layer on the stack being the count of the number of parameters in that scope
		Stack<Integer> stack = new Stack<Integer>();

		// push the 'global' scope
		stack.push(0);

		for (final Token token : rpn) {
			switch (token.type) {
			case UNARY_OPERATOR:
				if (stack.peek() < 1) {
					throw new FormulaException("Missing parameter(s) for operator " + token);
				}
				break;
			case OPERATOR:
				if (stack.peek() < 2) {
					throw new FormulaException("Missing parameter(s) for operator " + token);
				}
				// pop the operator's 2 parameters and add the result
				stack.set(stack.size() - 1, stack.peek() - 2 + 1);
				break;
			case FUNCTION:
				LazyFunction f = functions.get(token.surface.toUpperCase(Locale.ROOT));
				if (f == null) {
					throw new FormulaException("Unknown function '" + token + "' at position " + (token.pos + 1));
				}

				int numParams = stack.pop();
				if (!f.numParamsVaries() && numParams != f.getNumParams()) {
					throw new FormulaException("Function " + token + " expected " + f.getNumParams() + " parameters, got " + numParams);
				}
				if (stack.size() <= 0) {
					throw new FormulaException("Too many function calls, maximum scope exceeded");
				}
				// push the result of the function
				stack.set(stack.size() - 1, stack.peek() + 1);
				break;
			case OPEN_PAREN:
				stack.push(0);
				break;
			default:
				stack.set(stack.size() - 1, stack.peek() + 1);
			}
		}

		if (stack.size() > 1) {
			throw new FormulaException("Too many unhandled function parameter lists");
		} else if (stack.peek() > 1) {
			throw new FormulaException("Too many numbers or variables");
		} else if (stack.peek() < 1) {
			throw new FormulaException("Empty expression");
		}
	}

	/**
	 * Get a string representation of the RPN (Reverse Polish Notation) for this expression.
	 * 
	 * @return A string with the RPN representation for this expression.
	 */
	public String toRPN() {
		StringBuilder result = new StringBuilder();
		for (Token t : getRPN()) {
			if (result.length() != 0)
				result.append(" ");
			result.append(t.toString());
		}
		return result.toString();
	}

	/**
	 * Exposing declared variables in the expression.
	 * 
	 * @return All declared variables.
	 */
	public Set<String> getDeclaredVariables() {
		return Collections.unmodifiableSet(variables.keySet());
	}

	/**
	 * Exposing declared operators in the expression.
	 * 
	 * @return All declared operators.
	 */
	public Set<String> getDeclaredOperators() {
		return Collections.unmodifiableSet(operators.keySet());
	}

	/**
	 * Exposing declared functions.
	 * 
	 * @return All declared functions.
	 */
	public Set<String> getDeclaredFunctions() {
		return Collections.unmodifiableSet(functions.keySet());
	}

	/**
	 * @return The original expression string
	 */
	public String getExpression() {
		return expression;
	}

	/**
	 * Returns a list of the variables in the expression.
	 * 
	 * @return A list of the variable names in this expression.
	 */
	public List<String> getUsedVariables() {
		List<String> result = new ArrayList<String>();
		Tokenizer tokenizer = new Tokenizer(expression);
		while (tokenizer.hasNext()) {
			Token nextToken = tokenizer.next();
			String token = nextToken.toString();
			if (nextToken.type != TokenType.VARIABLE || token.equals("PI") || token.equals("e") || token.equals("TRUE") || token.equals("FALSE")) {
				continue;
			}
			result.add(token);
		}
		return result;
	}

	/**
	 * The original expression used to construct this expression, without variables substituted.
	 */
	public String getOriginalExpression() {
		return this.originalExpression;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Formula that = (Formula) o;
		if (this.expression == null) {
			return that.expression == null;
		} else {
			return this.expression.equals(that.expression);
		}
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		return this.expression == null ? 0 : this.expression.hashCode();
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return this.expression;
	}

	/**
	 * Checks whether the expression is a boolean expression. An expression is considered a boolean expression, if the last
	 * operator or function is boolean. The IF function is handled special. If the third parameter is boolean, then the IF
	 * is also considered boolean, else non-boolean.
	 * 
	 * @return <code>true</code> if the last operator/function was a boolean.
	 */
	public boolean isBoolean() {
		List<Token> rpn = getRPN();
		if (rpn.size() > 0) {
			for (int i = rpn.size() - 1; i >= 0; i--) {
				Token t = rpn.get(i);
				/*
				 * The IF function is handled special. If the third parameter is boolean, then the IF is also considered a boolean. Just
				 * skip the IF function to check the second parameter.
				 */
				if (t.surface.equals("IF"))
					continue;
				if (t.type == TokenType.FUNCTION) {
					return functions.get(t.surface).isBooleanFunction();
				} else if (t.type == TokenType.OPERATOR) {
					return operators.get(t.surface).isBooleanOperator();
				}
			}
		}
		return false;
	}

}
