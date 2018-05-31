package de.sonnmatt.muutos.parser;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.sonnmatt.muutos.parser.Formula.Function;
import de.sonnmatt.muutos.parser.Formula.LazyFunction;
import de.sonnmatt.muutos.parser.Formula.Operator;

public class Parser {

	static Logger log = LogManager.getLogger(Parser.class);
	
	private String source;
	private String langauage = "en-US";
	
	private String originalExpression = null;
	private String expression = null;
	private HashMap<String, Formula> formulas = new HashMap<String, Formula>();
	
	public Parser() {
	}

	public Parser(String expression) {
		setSourceText(expression);
	}

	public String getLangauage() {
		return langauage;
	}
	public void setLangauage(String langauage) {
		this.langauage = langauage;
	}
	public String getSource() {
		return source;
	}
	public void setSourceText(String source) {
		this.originalExpression = source;
		this.expression = source;
		this.formulas.clear();
		Matcher match = Pattern.compile("\\{([^}]+)\\}").matcher(source);
		while(match.find()) {
			formulas.put(match.group(1), new Formula(match.group(1)));
		}
	}
	
	public String eval() {
		return log.traceExit(".eval(): {}", this.eval(true));
	}

	public String eval(boolean stripTrailingZeros) {
//		for (Entry<String, Formula> entry : formulas.entrySet()) {
//			log.trace("Replace: {} -> {}", entry.getKey(), this.expression = this.expression.replace('{' + entry.getKey() + '}', entry.getValue().eval(stripTrailingZeros)));
//	    }
		formulas.forEach((k,v) -> this.expression = this.expression.replace('{' + k + '}', v.eval(stripTrailingZeros)));
		return log.traceExit("eval() result: {}", this.expression);
	}

	public Parser setPrecision(int precision) {
		formulas.forEach((k,v) -> v.setPrecision(precision));
		return this;
	}

	public Parser setRoundingMode(RoundingMode roundingMode) {
		formulas.forEach((k,v) -> v.setRoundingMode(roundingMode));
		return this;
	}

	public Parser setFirstVariableCharacters(String chars) {
		formulas.forEach((k,v) -> v.setFirstVariableCharacters(chars));
		return this;
	}

	public Parser setDateFormat(String dateTimeFormat) {
		formulas.forEach((k,v) -> v.setDateFormat(dateTimeFormat));
		return this;
	}

	public Parser setVariableCharacters(String chars) {
		formulas.forEach((k,v) -> v.setVariableCharacters(chars));
		return this;
	}

	public Operator addOperator(Operator operator) {
		formulas.forEach((k,v) -> v.addOperator(operator));
		return operator;
	}

	public Function addFunction(Function function) {
		formulas.forEach((k,v) -> v.addFunction(function));
		return function;
	}

	public LazyFunction addLazyFunction(LazyFunction function) {
		formulas.forEach((k,v) -> v.addLazyFunction(function));
		return function;
	}

	public Parser setVariable(String variable, BigDecimal value) {
		formulas.forEach((k,v) -> v.setVariable(variable, value));
		return this;
	}

	public Parser setVariable(String variable, String value) {
		formulas.forEach((k,v) -> v.setVariable(variable, value));
		return this;
	}

	public Parser with(String variable, BigDecimal value) {
		formulas.forEach((k,v) -> v.with(variable, value));
		return this;
	}

	public Parser with(String variable, String value) {
		formulas.forEach((k,v) -> v.with(variable, value));
		return this;
	}

	public Parser and(String variable, BigDecimal value) {
		formulas.forEach((k,v) -> v.and(variable, value));
		return this;
	}

	public Parser and(String variable, String value) {
		formulas.forEach((k,v) -> v.and(variable, value));
		return this;
	}

	public Set<String> getDeclaredVariables() {
		Set<String> declVars = new HashSet<String>();
		formulas.forEach((k,v) -> declVars.addAll(v.getDeclaredVariables()));
		return declVars;
	}

	public Set<String> getDeclaredOperators() {
		Set<String> declOp = new HashSet<String>();
		formulas.forEach((k,v) -> declOp.addAll(v.getDeclaredOperators()));
		return declOp;
	}

	public Set<String> getDeclaredFunctions() {
		Set<String> declFunc = new HashSet<String>();
		formulas.forEach((k,v) -> declFunc.addAll(v.getDeclaredFunctions()));
		return declFunc;
	}

	public String getExpression() {
		return this.originalExpression;
	}

	public List<String> getUsedVariables() {
		List<String> usedVars = new ArrayList<String>();
		formulas.forEach((k,v) -> usedVars.addAll(v.getDeclaredVariables()));
		return usedVars;
	}

	public String getOriginalExpression() {
		return this.originalExpression;
	}

	public boolean isBoolean() {
		Set<Boolean> isBool = new HashSet<Boolean>();
		formulas.forEach((k,v) -> isBool.add(v.isBoolean()));
		return (isBool.size() == 1);
	}
}