/*
 * generated by Xtext 2.10.0
 */
grammar InternalDsl;

options {
	superClass=AbstractInternalContentAssistParser;
}

@lexer::header {
package swi.dod.nxp.ide.contentassist.antlr.internal;

// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.ide.editor.contentassist.antlr.internal.Lexer;
}

@parser::header {
package swi.dod.nxp.ide.contentassist.antlr.internal;

import java.io.InputStream;
import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.ide.editor.contentassist.antlr.internal.AbstractInternalContentAssistParser;
import org.eclipse.xtext.ide.editor.contentassist.antlr.internal.DFA;
import swi.dod.nxp.services.DslGrammarAccess;

}
@parser::members {
	private DslGrammarAccess grammarAccess;

	public void setGrammarAccess(DslGrammarAccess grammarAccess) {
		this.grammarAccess = grammarAccess;
	}

	@Override
	protected Grammar getGrammar() {
		return grammarAccess.getGrammar();
	}

	@Override
	protected String getValueForTokenName(String tokenName) {
		return tokenName;
	}
}

// Entry rule entryRuleModel
entryRuleModel
:
{ before(grammarAccess.getModelRule()); }
	 ruleModel
{ after(grammarAccess.getModelRule()); } 
	 EOF 
;

// Rule Model
ruleModel 
	@init {
		int stackSize = keepStackSize();
	}
	:
	(
		{ before(grammarAccess.getModelAccess().getCommandsAssignment()); }
		(rule__Model__CommandsAssignment)*
		{ after(grammarAccess.getModelAccess().getCommandsAssignment()); }
	)
;
finally {
	restoreStackSize(stackSize);
}

// Entry rule entryRuleCommand
entryRuleCommand
:
{ before(grammarAccess.getCommandRule()); }
	 ruleCommand
{ after(grammarAccess.getCommandRule()); } 
	 EOF 
;

// Rule Command
ruleCommand 
	@init {
		int stackSize = keepStackSize();
	}
	:
	(
		{ before(grammarAccess.getCommandAccess().getGroup()); }
		(rule__Command__Group__0)
		{ after(grammarAccess.getCommandAccess().getGroup()); }
	)
;
finally {
	restoreStackSize(stackSize);
}

// Entry rule entryRuleEngine
entryRuleEngine
:
{ before(grammarAccess.getEngineRule()); }
	 ruleEngine
{ after(grammarAccess.getEngineRule()); } 
	 EOF 
;

// Rule Engine
ruleEngine 
	@init {
		int stackSize = keepStackSize();
	}
	:
	(
		{ before(grammarAccess.getEngineAccess().getGroup()); }
		(rule__Engine__Group__0)
		{ after(grammarAccess.getEngineAccess().getGroup()); }
	)
;
finally {
	restoreStackSize(stackSize);
}

// Entry rule entryRuleNavigate
entryRuleNavigate
:
{ before(grammarAccess.getNavigateRule()); }
	 ruleNavigate
{ after(grammarAccess.getNavigateRule()); } 
	 EOF 
;

// Rule Navigate
ruleNavigate 
	@init {
		int stackSize = keepStackSize();
	}
	:
	(
		{ before(grammarAccess.getNavigateAccess().getGroup()); }
		(rule__Navigate__Group__0)
		{ after(grammarAccess.getNavigateAccess().getGroup()); }
	)
;
finally {
	restoreStackSize(stackSize);
}

// Entry rule entryRuleRun
entryRuleRun
:
{ before(grammarAccess.getRunRule()); }
	 ruleRun
{ after(grammarAccess.getRunRule()); } 
	 EOF 
;

// Rule Run
ruleRun 
	@init {
		int stackSize = keepStackSize();
	}
	:
	(
		{ before(grammarAccess.getRunAccess().getGroup()); }
		(rule__Run__Group__0)
		{ after(grammarAccess.getRunAccess().getGroup()); }
	)
;
finally {
	restoreStackSize(stackSize);
}

// Entry rule entryRulePercentage
entryRulePercentage
:
{ before(grammarAccess.getPercentageRule()); }
	 rulePercentage
{ after(grammarAccess.getPercentageRule()); } 
	 EOF 
;

// Rule Percentage
rulePercentage 
	@init {
		int stackSize = keepStackSize();
	}
	:
	(
		{ before(grammarAccess.getPercentageAccess().getGroup()); }
		(rule__Percentage__Group__0)
		{ after(grammarAccess.getPercentageAccess().getGroup()); }
	)
;
finally {
	restoreStackSize(stackSize);
}

// Entry rule entryRuleAngle
entryRuleAngle
:
{ before(grammarAccess.getAngleRule()); }
	 ruleAngle
{ after(grammarAccess.getAngleRule()); } 
	 EOF 
;

// Rule Angle
ruleAngle 
	@init {
		int stackSize = keepStackSize();
	}
	:
	(
		{ before(grammarAccess.getAngleAccess().getFloatParserRuleCall()); }
		ruleFloat
		{ after(grammarAccess.getAngleAccess().getFloatParserRuleCall()); }
	)
;
finally {
	restoreStackSize(stackSize);
}

// Entry rule entryRuleTime
entryRuleTime
:
{ before(grammarAccess.getTimeRule()); }
	 ruleTime
{ after(grammarAccess.getTimeRule()); } 
	 EOF 
;

// Rule Time
ruleTime 
	@init {
		int stackSize = keepStackSize();
	}
	:
	(
		{ before(grammarAccess.getTimeAccess().getGroup()); }
		(rule__Time__Group__0)
		{ after(grammarAccess.getTimeAccess().getGroup()); }
	)
;
finally {
	restoreStackSize(stackSize);
}

// Entry rule entryRuleFloat
entryRuleFloat
:
{ before(grammarAccess.getFloatRule()); }
	 ruleFloat
{ after(grammarAccess.getFloatRule()); } 
	 EOF 
;

// Rule Float
ruleFloat 
	@init {
		int stackSize = keepStackSize();
	}
	:
	(
		{ before(grammarAccess.getFloatAccess().getGroup()); }
		(rule__Float__Group__0)
		{ after(grammarAccess.getFloatAccess().getGroup()); }
	)
;
finally {
	restoreStackSize(stackSize);
}

rule__Command__Alternatives_0
	@init {
		int stackSize = keepStackSize();
	}
:
	(
		{ before(grammarAccess.getCommandAccess().getEngineParserRuleCall_0_0()); }
		ruleEngine
		{ after(grammarAccess.getCommandAccess().getEngineParserRuleCall_0_0()); }
	)
	|
	(
		{ before(grammarAccess.getCommandAccess().getNavigateParserRuleCall_0_1()); }
		ruleNavigate
		{ after(grammarAccess.getCommandAccess().getNavigateParserRuleCall_0_1()); }
	)
	|
	(
		{ before(grammarAccess.getCommandAccess().getRunParserRuleCall_0_2()); }
		ruleRun
		{ after(grammarAccess.getCommandAccess().getRunParserRuleCall_0_2()); }
	)
;
finally {
	restoreStackSize(stackSize);
}

rule__Float__Alternatives_0
	@init {
		int stackSize = keepStackSize();
	}
:
	(
		{ before(grammarAccess.getFloatAccess().getNONZERO_DIGITTerminalRuleCall_0_0()); }
		RULE_NONZERO_DIGIT
		{ after(grammarAccess.getFloatAccess().getNONZERO_DIGITTerminalRuleCall_0_0()); }
	)
	|
	(
		{ before(grammarAccess.getFloatAccess().getZEROTerminalRuleCall_0_1()); }
		RULE_ZERO
		{ after(grammarAccess.getFloatAccess().getZEROTerminalRuleCall_0_1()); }
	)
;
finally {
	restoreStackSize(stackSize);
}

rule__Float__Alternatives_1_1
	@init {
		int stackSize = keepStackSize();
	}
:
	(
		{ before(grammarAccess.getFloatAccess().getNONZERO_DIGITTerminalRuleCall_1_1_0()); }
		RULE_NONZERO_DIGIT
		{ after(grammarAccess.getFloatAccess().getNONZERO_DIGITTerminalRuleCall_1_1_0()); }
	)
	|
	(
		{ before(grammarAccess.getFloatAccess().getZEROTerminalRuleCall_1_1_1()); }
		RULE_ZERO
		{ after(grammarAccess.getFloatAccess().getZEROTerminalRuleCall_1_1_1()); }
	)
;
finally {
	restoreStackSize(stackSize);
}

rule__Command__Group__0
	@init {
		int stackSize = keepStackSize();
	}
:
	rule__Command__Group__0__Impl
	rule__Command__Group__1
;
finally {
	restoreStackSize(stackSize);
}

rule__Command__Group__0__Impl
	@init {
		int stackSize = keepStackSize();
	}
:
(
	{ before(grammarAccess.getCommandAccess().getAlternatives_0()); }
	(rule__Command__Alternatives_0)
	{ after(grammarAccess.getCommandAccess().getAlternatives_0()); }
)
;
finally {
	restoreStackSize(stackSize);
}

rule__Command__Group__1
	@init {
		int stackSize = keepStackSize();
	}
:
	rule__Command__Group__1__Impl
;
finally {
	restoreStackSize(stackSize);
}

rule__Command__Group__1__Impl
	@init {
		int stackSize = keepStackSize();
	}
:
(
	{ before(grammarAccess.getCommandAccess().getFullStopKeyword_1()); }
	'.'
	{ after(grammarAccess.getCommandAccess().getFullStopKeyword_1()); }
)
;
finally {
	restoreStackSize(stackSize);
}


rule__Engine__Group__0
	@init {
		int stackSize = keepStackSize();
	}
:
	rule__Engine__Group__0__Impl
	rule__Engine__Group__1
;
finally {
	restoreStackSize(stackSize);
}

rule__Engine__Group__0__Impl
	@init {
		int stackSize = keepStackSize();
	}
:
(
	{ before(grammarAccess.getEngineAccess().getEngineKeyword_0()); }
	'Engine'
	{ after(grammarAccess.getEngineAccess().getEngineKeyword_0()); }
)
;
finally {
	restoreStackSize(stackSize);
}

rule__Engine__Group__1
	@init {
		int stackSize = keepStackSize();
	}
:
	rule__Engine__Group__1__Impl
	rule__Engine__Group__2
;
finally {
	restoreStackSize(stackSize);
}

rule__Engine__Group__1__Impl
	@init {
		int stackSize = keepStackSize();
	}
:
(
	{ before(grammarAccess.getEngineAccess().getDIRECTION_LRTerminalRuleCall_1()); }
	(RULE_DIRECTION_LR)?
	{ after(grammarAccess.getEngineAccess().getDIRECTION_LRTerminalRuleCall_1()); }
)
;
finally {
	restoreStackSize(stackSize);
}

rule__Engine__Group__2
	@init {
		int stackSize = keepStackSize();
	}
:
	rule__Engine__Group__2__Impl
	rule__Engine__Group__3
;
finally {
	restoreStackSize(stackSize);
}

rule__Engine__Group__2__Impl
	@init {
		int stackSize = keepStackSize();
	}
:
(
	{ before(grammarAccess.getEngineAccess().getDIRECTIONTerminalRuleCall_2()); }
	RULE_DIRECTION
	{ after(grammarAccess.getEngineAccess().getDIRECTIONTerminalRuleCall_2()); }
)
;
finally {
	restoreStackSize(stackSize);
}

rule__Engine__Group__3
	@init {
		int stackSize = keepStackSize();
	}
:
	rule__Engine__Group__3__Impl
;
finally {
	restoreStackSize(stackSize);
}

rule__Engine__Group__3__Impl
	@init {
		int stackSize = keepStackSize();
	}
:
(
	{ before(grammarAccess.getEngineAccess().getPercentageParserRuleCall_3()); }
	rulePercentage
	{ after(grammarAccess.getEngineAccess().getPercentageParserRuleCall_3()); }
)
;
finally {
	restoreStackSize(stackSize);
}


rule__Navigate__Group__0
	@init {
		int stackSize = keepStackSize();
	}
:
	rule__Navigate__Group__0__Impl
	rule__Navigate__Group__1
;
finally {
	restoreStackSize(stackSize);
}

rule__Navigate__Group__0__Impl
	@init {
		int stackSize = keepStackSize();
	}
:
(
	{ before(grammarAccess.getNavigateAccess().getNavigateKeyword_0()); }
	'Navigate'
	{ after(grammarAccess.getNavigateAccess().getNavigateKeyword_0()); }
)
;
finally {
	restoreStackSize(stackSize);
}

rule__Navigate__Group__1
	@init {
		int stackSize = keepStackSize();
	}
:
	rule__Navigate__Group__1__Impl
	rule__Navigate__Group__2
;
finally {
	restoreStackSize(stackSize);
}

rule__Navigate__Group__1__Impl
	@init {
		int stackSize = keepStackSize();
	}
:
(
	{ before(grammarAccess.getNavigateAccess().getDIRECTION_LRTerminalRuleCall_1()); }
	RULE_DIRECTION_LR
	{ after(grammarAccess.getNavigateAccess().getDIRECTION_LRTerminalRuleCall_1()); }
)
;
finally {
	restoreStackSize(stackSize);
}

rule__Navigate__Group__2
	@init {
		int stackSize = keepStackSize();
	}
:
	rule__Navigate__Group__2__Impl
;
finally {
	restoreStackSize(stackSize);
}

rule__Navigate__Group__2__Impl
	@init {
		int stackSize = keepStackSize();
	}
:
(
	{ before(grammarAccess.getNavigateAccess().getAngleParserRuleCall_2()); }
	ruleAngle
	{ after(grammarAccess.getNavigateAccess().getAngleParserRuleCall_2()); }
)
;
finally {
	restoreStackSize(stackSize);
}


rule__Run__Group__0
	@init {
		int stackSize = keepStackSize();
	}
:
	rule__Run__Group__0__Impl
	rule__Run__Group__1
;
finally {
	restoreStackSize(stackSize);
}

rule__Run__Group__0__Impl
	@init {
		int stackSize = keepStackSize();
	}
:
(
	{ before(grammarAccess.getRunAccess().getRunKeyword_0()); }
	'Run'
	{ after(grammarAccess.getRunAccess().getRunKeyword_0()); }
)
;
finally {
	restoreStackSize(stackSize);
}

rule__Run__Group__1
	@init {
		int stackSize = keepStackSize();
	}
:
	rule__Run__Group__1__Impl
;
finally {
	restoreStackSize(stackSize);
}

rule__Run__Group__1__Impl
	@init {
		int stackSize = keepStackSize();
	}
:
(
	{ before(grammarAccess.getRunAccess().getTimeParserRuleCall_1()); }
	ruleTime
	{ after(grammarAccess.getRunAccess().getTimeParserRuleCall_1()); }
)
;
finally {
	restoreStackSize(stackSize);
}


rule__Percentage__Group__0
	@init {
		int stackSize = keepStackSize();
	}
:
	rule__Percentage__Group__0__Impl
	rule__Percentage__Group__1
;
finally {
	restoreStackSize(stackSize);
}

rule__Percentage__Group__0__Impl
	@init {
		int stackSize = keepStackSize();
	}
:
(
	{ before(grammarAccess.getPercentageAccess().getFloatParserRuleCall_0()); }
	ruleFloat
	{ after(grammarAccess.getPercentageAccess().getFloatParserRuleCall_0()); }
)
;
finally {
	restoreStackSize(stackSize);
}

rule__Percentage__Group__1
	@init {
		int stackSize = keepStackSize();
	}
:
	rule__Percentage__Group__1__Impl
;
finally {
	restoreStackSize(stackSize);
}

rule__Percentage__Group__1__Impl
	@init {
		int stackSize = keepStackSize();
	}
:
(
	{ before(grammarAccess.getPercentageAccess().getPercentSignKeyword_1()); }
	'%'
	{ after(grammarAccess.getPercentageAccess().getPercentSignKeyword_1()); }
)
;
finally {
	restoreStackSize(stackSize);
}


rule__Time__Group__0
	@init {
		int stackSize = keepStackSize();
	}
:
	rule__Time__Group__0__Impl
	rule__Time__Group__1
;
finally {
	restoreStackSize(stackSize);
}

rule__Time__Group__0__Impl
	@init {
		int stackSize = keepStackSize();
	}
:
(
	{ before(grammarAccess.getTimeAccess().getFloatParserRuleCall_0()); }
	ruleFloat
	{ after(grammarAccess.getTimeAccess().getFloatParserRuleCall_0()); }
)
;
finally {
	restoreStackSize(stackSize);
}

rule__Time__Group__1
	@init {
		int stackSize = keepStackSize();
	}
:
	rule__Time__Group__1__Impl
;
finally {
	restoreStackSize(stackSize);
}

rule__Time__Group__1__Impl
	@init {
		int stackSize = keepStackSize();
	}
:
(
	{ before(grammarAccess.getTimeAccess().getUNITSTerminalRuleCall_1()); }
	RULE_UNITS
	{ after(grammarAccess.getTimeAccess().getUNITSTerminalRuleCall_1()); }
)
;
finally {
	restoreStackSize(stackSize);
}


rule__Float__Group__0
	@init {
		int stackSize = keepStackSize();
	}
:
	rule__Float__Group__0__Impl
	rule__Float__Group__1
;
finally {
	restoreStackSize(stackSize);
}

rule__Float__Group__0__Impl
	@init {
		int stackSize = keepStackSize();
	}
:
(
	(
		{ before(grammarAccess.getFloatAccess().getAlternatives_0()); }
		(rule__Float__Alternatives_0)
		{ after(grammarAccess.getFloatAccess().getAlternatives_0()); }
	)
	(
		{ before(grammarAccess.getFloatAccess().getAlternatives_0()); }
		(rule__Float__Alternatives_0)*
		{ after(grammarAccess.getFloatAccess().getAlternatives_0()); }
	)
)
;
finally {
	restoreStackSize(stackSize);
}

rule__Float__Group__1
	@init {
		int stackSize = keepStackSize();
	}
:
	rule__Float__Group__1__Impl
;
finally {
	restoreStackSize(stackSize);
}

rule__Float__Group__1__Impl
	@init {
		int stackSize = keepStackSize();
	}
:
(
	{ before(grammarAccess.getFloatAccess().getGroup_1()); }
	(rule__Float__Group_1__0)?
	{ after(grammarAccess.getFloatAccess().getGroup_1()); }
)
;
finally {
	restoreStackSize(stackSize);
}


rule__Float__Group_1__0
	@init {
		int stackSize = keepStackSize();
	}
:
	rule__Float__Group_1__0__Impl
	rule__Float__Group_1__1
;
finally {
	restoreStackSize(stackSize);
}

rule__Float__Group_1__0__Impl
	@init {
		int stackSize = keepStackSize();
	}
:
(
	{ before(grammarAccess.getFloatAccess().getCommaKeyword_1_0()); }
	','
	{ after(grammarAccess.getFloatAccess().getCommaKeyword_1_0()); }
)
;
finally {
	restoreStackSize(stackSize);
}

rule__Float__Group_1__1
	@init {
		int stackSize = keepStackSize();
	}
:
	rule__Float__Group_1__1__Impl
;
finally {
	restoreStackSize(stackSize);
}

rule__Float__Group_1__1__Impl
	@init {
		int stackSize = keepStackSize();
	}
:
(
	(
		{ before(grammarAccess.getFloatAccess().getAlternatives_1_1()); }
		(rule__Float__Alternatives_1_1)
		{ after(grammarAccess.getFloatAccess().getAlternatives_1_1()); }
	)
	(
		{ before(grammarAccess.getFloatAccess().getAlternatives_1_1()); }
		(rule__Float__Alternatives_1_1)*
		{ after(grammarAccess.getFloatAccess().getAlternatives_1_1()); }
	)
)
;
finally {
	restoreStackSize(stackSize);
}


rule__Model__CommandsAssignment
	@init {
		int stackSize = keepStackSize();
	}
:
	(
		{ before(grammarAccess.getModelAccess().getCommandsCommandParserRuleCall_0()); }
		ruleCommand
		{ after(grammarAccess.getModelAccess().getCommandsCommandParserRuleCall_0()); }
	)
;
finally {
	restoreStackSize(stackSize);
}

RULE_ZERO : '0';

RULE_NONZERO_DIGIT : '1'..'9';

RULE_DIRECTION_LR : ('left'|'right');

RULE_DIRECTION : ('forward'|'backward');

RULE_UNITS : ('s'|'ms');

RULE_WS : (' '|'\t'|'\r'|'\n')+;

RULE_ML_COMMENT : '/*' ( options {greedy=false;} : . )*'*/';

RULE_SL_COMMENT : '//' ~(('\n'|'\r'))* ('\r'? '\n')?;
