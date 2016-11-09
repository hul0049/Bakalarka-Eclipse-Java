package swi.dod.nxp.parser.antlr.internal;

import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.AbstractInternalAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.parser.antlr.AntlrDatatypeRuleToken;
import swi.dod.nxp.services.DslGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalDslParser extends AbstractInternalAntlrParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_DIRECTION_LR", "RULE_DIRECTION", "RULE_UNITS", "RULE_NONZERO_DIGIT", "RULE_ZERO", "RULE_WS", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "'.'", "'Engine'", "'Navigate'", "'Run'", "'%'", "','"
    };
    public static final int RULE_DIRECTION_LR=4;
    public static final int RULE_WS=9;
    public static final int RULE_ZERO=8;
    public static final int RULE_SL_COMMENT=11;
    public static final int T__15=15;
    public static final int T__16=16;
    public static final int T__17=17;
    public static final int RULE_ML_COMMENT=10;
    public static final int T__12=12;
    public static final int RULE_UNITS=6;
    public static final int T__13=13;
    public static final int RULE_DIRECTION=5;
    public static final int RULE_NONZERO_DIGIT=7;
    public static final int T__14=14;
    public static final int EOF=-1;

    // delegates
    // delegators


        public InternalDslParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public InternalDslParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return InternalDslParser.tokenNames; }
    public String getGrammarFileName() { return "InternalDsl.g"; }



     	private DslGrammarAccess grammarAccess;

        public InternalDslParser(TokenStream input, DslGrammarAccess grammarAccess) {
            this(input);
            this.grammarAccess = grammarAccess;
            registerRules(grammarAccess.getGrammar());
        }

        @Override
        protected String getFirstRuleName() {
        	return "Model";
       	}

       	@Override
       	protected DslGrammarAccess getGrammarAccess() {
       		return grammarAccess;
       	}




    // $ANTLR start "entryRuleModel"
    // InternalDsl.g:64:1: entryRuleModel returns [EObject current=null] : iv_ruleModel= ruleModel EOF ;
    public final EObject entryRuleModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleModel = null;


        try {
            // InternalDsl.g:64:46: (iv_ruleModel= ruleModel EOF )
            // InternalDsl.g:65:2: iv_ruleModel= ruleModel EOF
            {
             newCompositeNode(grammarAccess.getModelRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleModel=ruleModel();

            state._fsp--;

             current =iv_ruleModel; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleModel"


    // $ANTLR start "ruleModel"
    // InternalDsl.g:71:1: ruleModel returns [EObject current=null] : ( (lv_commands_0_0= ruleCommand ) )* ;
    public final EObject ruleModel() throws RecognitionException {
        EObject current = null;

        AntlrDatatypeRuleToken lv_commands_0_0 = null;



        	enterRule();

        try {
            // InternalDsl.g:77:2: ( ( (lv_commands_0_0= ruleCommand ) )* )
            // InternalDsl.g:78:2: ( (lv_commands_0_0= ruleCommand ) )*
            {
            // InternalDsl.g:78:2: ( (lv_commands_0_0= ruleCommand ) )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>=13 && LA1_0<=15)) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // InternalDsl.g:79:3: (lv_commands_0_0= ruleCommand )
            	    {
            	    // InternalDsl.g:79:3: (lv_commands_0_0= ruleCommand )
            	    // InternalDsl.g:80:4: lv_commands_0_0= ruleCommand
            	    {

            	    				newCompositeNode(grammarAccess.getModelAccess().getCommandsCommandParserRuleCall_0());
            	    			
            	    pushFollow(FOLLOW_3);
            	    lv_commands_0_0=ruleCommand();

            	    state._fsp--;


            	    				if (current==null) {
            	    					current = createModelElementForParent(grammarAccess.getModelRule());
            	    				}
            	    				add(
            	    					current,
            	    					"commands",
            	    					lv_commands_0_0,
            	    					"swi.dod.nxp.Dsl.Command");
            	    				afterParserOrEnumRuleCall();
            	    			

            	    }


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleModel"


    // $ANTLR start "entryRuleCommand"
    // InternalDsl.g:100:1: entryRuleCommand returns [String current=null] : iv_ruleCommand= ruleCommand EOF ;
    public final String entryRuleCommand() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleCommand = null;


        try {
            // InternalDsl.g:100:47: (iv_ruleCommand= ruleCommand EOF )
            // InternalDsl.g:101:2: iv_ruleCommand= ruleCommand EOF
            {
             newCompositeNode(grammarAccess.getCommandRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleCommand=ruleCommand();

            state._fsp--;

             current =iv_ruleCommand.getText(); 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleCommand"


    // $ANTLR start "ruleCommand"
    // InternalDsl.g:107:1: ruleCommand returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : ( (this_Engine_0= ruleEngine | this_Navigate_1= ruleNavigate | this_Run_2= ruleRun ) kw= '.' ) ;
    public final AntlrDatatypeRuleToken ruleCommand() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;
        AntlrDatatypeRuleToken this_Engine_0 = null;

        AntlrDatatypeRuleToken this_Navigate_1 = null;

        AntlrDatatypeRuleToken this_Run_2 = null;



        	enterRule();

        try {
            // InternalDsl.g:113:2: ( ( (this_Engine_0= ruleEngine | this_Navigate_1= ruleNavigate | this_Run_2= ruleRun ) kw= '.' ) )
            // InternalDsl.g:114:2: ( (this_Engine_0= ruleEngine | this_Navigate_1= ruleNavigate | this_Run_2= ruleRun ) kw= '.' )
            {
            // InternalDsl.g:114:2: ( (this_Engine_0= ruleEngine | this_Navigate_1= ruleNavigate | this_Run_2= ruleRun ) kw= '.' )
            // InternalDsl.g:115:3: (this_Engine_0= ruleEngine | this_Navigate_1= ruleNavigate | this_Run_2= ruleRun ) kw= '.'
            {
            // InternalDsl.g:115:3: (this_Engine_0= ruleEngine | this_Navigate_1= ruleNavigate | this_Run_2= ruleRun )
            int alt2=3;
            switch ( input.LA(1) ) {
            case 13:
                {
                alt2=1;
                }
                break;
            case 14:
                {
                alt2=2;
                }
                break;
            case 15:
                {
                alt2=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }

            switch (alt2) {
                case 1 :
                    // InternalDsl.g:116:4: this_Engine_0= ruleEngine
                    {

                    				newCompositeNode(grammarAccess.getCommandAccess().getEngineParserRuleCall_0_0());
                    			
                    pushFollow(FOLLOW_4);
                    this_Engine_0=ruleEngine();

                    state._fsp--;


                    				current.merge(this_Engine_0);
                    			

                    				afterParserOrEnumRuleCall();
                    			

                    }
                    break;
                case 2 :
                    // InternalDsl.g:127:4: this_Navigate_1= ruleNavigate
                    {

                    				newCompositeNode(grammarAccess.getCommandAccess().getNavigateParserRuleCall_0_1());
                    			
                    pushFollow(FOLLOW_4);
                    this_Navigate_1=ruleNavigate();

                    state._fsp--;


                    				current.merge(this_Navigate_1);
                    			

                    				afterParserOrEnumRuleCall();
                    			

                    }
                    break;
                case 3 :
                    // InternalDsl.g:138:4: this_Run_2= ruleRun
                    {

                    				newCompositeNode(grammarAccess.getCommandAccess().getRunParserRuleCall_0_2());
                    			
                    pushFollow(FOLLOW_4);
                    this_Run_2=ruleRun();

                    state._fsp--;


                    				current.merge(this_Run_2);
                    			

                    				afterParserOrEnumRuleCall();
                    			

                    }
                    break;

            }

            kw=(Token)match(input,12,FOLLOW_2); 

            			current.merge(kw);
            			newLeafNode(kw, grammarAccess.getCommandAccess().getFullStopKeyword_1());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleCommand"


    // $ANTLR start "entryRuleEngine"
    // InternalDsl.g:158:1: entryRuleEngine returns [String current=null] : iv_ruleEngine= ruleEngine EOF ;
    public final String entryRuleEngine() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleEngine = null;


        try {
            // InternalDsl.g:158:46: (iv_ruleEngine= ruleEngine EOF )
            // InternalDsl.g:159:2: iv_ruleEngine= ruleEngine EOF
            {
             newCompositeNode(grammarAccess.getEngineRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleEngine=ruleEngine();

            state._fsp--;

             current =iv_ruleEngine.getText(); 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleEngine"


    // $ANTLR start "ruleEngine"
    // InternalDsl.g:165:1: ruleEngine returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= 'Engine' (this_DIRECTION_LR_1= RULE_DIRECTION_LR )? this_DIRECTION_2= RULE_DIRECTION this_Percentage_3= rulePercentage ) ;
    public final AntlrDatatypeRuleToken ruleEngine() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;
        Token this_DIRECTION_LR_1=null;
        Token this_DIRECTION_2=null;
        AntlrDatatypeRuleToken this_Percentage_3 = null;



        	enterRule();

        try {
            // InternalDsl.g:171:2: ( (kw= 'Engine' (this_DIRECTION_LR_1= RULE_DIRECTION_LR )? this_DIRECTION_2= RULE_DIRECTION this_Percentage_3= rulePercentage ) )
            // InternalDsl.g:172:2: (kw= 'Engine' (this_DIRECTION_LR_1= RULE_DIRECTION_LR )? this_DIRECTION_2= RULE_DIRECTION this_Percentage_3= rulePercentage )
            {
            // InternalDsl.g:172:2: (kw= 'Engine' (this_DIRECTION_LR_1= RULE_DIRECTION_LR )? this_DIRECTION_2= RULE_DIRECTION this_Percentage_3= rulePercentage )
            // InternalDsl.g:173:3: kw= 'Engine' (this_DIRECTION_LR_1= RULE_DIRECTION_LR )? this_DIRECTION_2= RULE_DIRECTION this_Percentage_3= rulePercentage
            {
            kw=(Token)match(input,13,FOLLOW_5); 

            			current.merge(kw);
            			newLeafNode(kw, grammarAccess.getEngineAccess().getEngineKeyword_0());
            		
            // InternalDsl.g:178:3: (this_DIRECTION_LR_1= RULE_DIRECTION_LR )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==RULE_DIRECTION_LR) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // InternalDsl.g:179:4: this_DIRECTION_LR_1= RULE_DIRECTION_LR
                    {
                    this_DIRECTION_LR_1=(Token)match(input,RULE_DIRECTION_LR,FOLLOW_6); 

                    				current.merge(this_DIRECTION_LR_1);
                    			

                    				newLeafNode(this_DIRECTION_LR_1, grammarAccess.getEngineAccess().getDIRECTION_LRTerminalRuleCall_1());
                    			

                    }
                    break;

            }

            this_DIRECTION_2=(Token)match(input,RULE_DIRECTION,FOLLOW_7); 

            			current.merge(this_DIRECTION_2);
            		

            			newLeafNode(this_DIRECTION_2, grammarAccess.getEngineAccess().getDIRECTIONTerminalRuleCall_2());
            		

            			newCompositeNode(grammarAccess.getEngineAccess().getPercentageParserRuleCall_3());
            		
            pushFollow(FOLLOW_2);
            this_Percentage_3=rulePercentage();

            state._fsp--;


            			current.merge(this_Percentage_3);
            		

            			afterParserOrEnumRuleCall();
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleEngine"


    // $ANTLR start "entryRuleNavigate"
    // InternalDsl.g:208:1: entryRuleNavigate returns [String current=null] : iv_ruleNavigate= ruleNavigate EOF ;
    public final String entryRuleNavigate() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleNavigate = null;


        try {
            // InternalDsl.g:208:48: (iv_ruleNavigate= ruleNavigate EOF )
            // InternalDsl.g:209:2: iv_ruleNavigate= ruleNavigate EOF
            {
             newCompositeNode(grammarAccess.getNavigateRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleNavigate=ruleNavigate();

            state._fsp--;

             current =iv_ruleNavigate.getText(); 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleNavigate"


    // $ANTLR start "ruleNavigate"
    // InternalDsl.g:215:1: ruleNavigate returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= 'Navigate' this_DIRECTION_LR_1= RULE_DIRECTION_LR this_Angle_2= ruleAngle ) ;
    public final AntlrDatatypeRuleToken ruleNavigate() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;
        Token this_DIRECTION_LR_1=null;
        AntlrDatatypeRuleToken this_Angle_2 = null;



        	enterRule();

        try {
            // InternalDsl.g:221:2: ( (kw= 'Navigate' this_DIRECTION_LR_1= RULE_DIRECTION_LR this_Angle_2= ruleAngle ) )
            // InternalDsl.g:222:2: (kw= 'Navigate' this_DIRECTION_LR_1= RULE_DIRECTION_LR this_Angle_2= ruleAngle )
            {
            // InternalDsl.g:222:2: (kw= 'Navigate' this_DIRECTION_LR_1= RULE_DIRECTION_LR this_Angle_2= ruleAngle )
            // InternalDsl.g:223:3: kw= 'Navigate' this_DIRECTION_LR_1= RULE_DIRECTION_LR this_Angle_2= ruleAngle
            {
            kw=(Token)match(input,14,FOLLOW_8); 

            			current.merge(kw);
            			newLeafNode(kw, grammarAccess.getNavigateAccess().getNavigateKeyword_0());
            		
            this_DIRECTION_LR_1=(Token)match(input,RULE_DIRECTION_LR,FOLLOW_7); 

            			current.merge(this_DIRECTION_LR_1);
            		

            			newLeafNode(this_DIRECTION_LR_1, grammarAccess.getNavigateAccess().getDIRECTION_LRTerminalRuleCall_1());
            		

            			newCompositeNode(grammarAccess.getNavigateAccess().getAngleParserRuleCall_2());
            		
            pushFollow(FOLLOW_2);
            this_Angle_2=ruleAngle();

            state._fsp--;


            			current.merge(this_Angle_2);
            		

            			afterParserOrEnumRuleCall();
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleNavigate"


    // $ANTLR start "entryRuleRun"
    // InternalDsl.g:249:1: entryRuleRun returns [String current=null] : iv_ruleRun= ruleRun EOF ;
    public final String entryRuleRun() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleRun = null;


        try {
            // InternalDsl.g:249:43: (iv_ruleRun= ruleRun EOF )
            // InternalDsl.g:250:2: iv_ruleRun= ruleRun EOF
            {
             newCompositeNode(grammarAccess.getRunRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleRun=ruleRun();

            state._fsp--;

             current =iv_ruleRun.getText(); 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleRun"


    // $ANTLR start "ruleRun"
    // InternalDsl.g:256:1: ruleRun returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= 'Run' this_Time_1= ruleTime ) ;
    public final AntlrDatatypeRuleToken ruleRun() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;
        AntlrDatatypeRuleToken this_Time_1 = null;



        	enterRule();

        try {
            // InternalDsl.g:262:2: ( (kw= 'Run' this_Time_1= ruleTime ) )
            // InternalDsl.g:263:2: (kw= 'Run' this_Time_1= ruleTime )
            {
            // InternalDsl.g:263:2: (kw= 'Run' this_Time_1= ruleTime )
            // InternalDsl.g:264:3: kw= 'Run' this_Time_1= ruleTime
            {
            kw=(Token)match(input,15,FOLLOW_7); 

            			current.merge(kw);
            			newLeafNode(kw, grammarAccess.getRunAccess().getRunKeyword_0());
            		

            			newCompositeNode(grammarAccess.getRunAccess().getTimeParserRuleCall_1());
            		
            pushFollow(FOLLOW_2);
            this_Time_1=ruleTime();

            state._fsp--;


            			current.merge(this_Time_1);
            		

            			afterParserOrEnumRuleCall();
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleRun"


    // $ANTLR start "entryRulePercentage"
    // InternalDsl.g:283:1: entryRulePercentage returns [String current=null] : iv_rulePercentage= rulePercentage EOF ;
    public final String entryRulePercentage() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_rulePercentage = null;


        try {
            // InternalDsl.g:283:50: (iv_rulePercentage= rulePercentage EOF )
            // InternalDsl.g:284:2: iv_rulePercentage= rulePercentage EOF
            {
             newCompositeNode(grammarAccess.getPercentageRule()); 
            pushFollow(FOLLOW_1);
            iv_rulePercentage=rulePercentage();

            state._fsp--;

             current =iv_rulePercentage.getText(); 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRulePercentage"


    // $ANTLR start "rulePercentage"
    // InternalDsl.g:290:1: rulePercentage returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_Float_0= ruleFloat kw= '%' ) ;
    public final AntlrDatatypeRuleToken rulePercentage() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;
        AntlrDatatypeRuleToken this_Float_0 = null;



        	enterRule();

        try {
            // InternalDsl.g:296:2: ( (this_Float_0= ruleFloat kw= '%' ) )
            // InternalDsl.g:297:2: (this_Float_0= ruleFloat kw= '%' )
            {
            // InternalDsl.g:297:2: (this_Float_0= ruleFloat kw= '%' )
            // InternalDsl.g:298:3: this_Float_0= ruleFloat kw= '%'
            {

            			newCompositeNode(grammarAccess.getPercentageAccess().getFloatParserRuleCall_0());
            		
            pushFollow(FOLLOW_9);
            this_Float_0=ruleFloat();

            state._fsp--;


            			current.merge(this_Float_0);
            		

            			afterParserOrEnumRuleCall();
            		
            kw=(Token)match(input,16,FOLLOW_2); 

            			current.merge(kw);
            			newLeafNode(kw, grammarAccess.getPercentageAccess().getPercentSignKeyword_1());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "rulePercentage"


    // $ANTLR start "entryRuleAngle"
    // InternalDsl.g:317:1: entryRuleAngle returns [String current=null] : iv_ruleAngle= ruleAngle EOF ;
    public final String entryRuleAngle() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleAngle = null;


        try {
            // InternalDsl.g:317:45: (iv_ruleAngle= ruleAngle EOF )
            // InternalDsl.g:318:2: iv_ruleAngle= ruleAngle EOF
            {
             newCompositeNode(grammarAccess.getAngleRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAngle=ruleAngle();

            state._fsp--;

             current =iv_ruleAngle.getText(); 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAngle"


    // $ANTLR start "ruleAngle"
    // InternalDsl.g:324:1: ruleAngle returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : this_Float_0= ruleFloat ;
    public final AntlrDatatypeRuleToken ruleAngle() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        AntlrDatatypeRuleToken this_Float_0 = null;



        	enterRule();

        try {
            // InternalDsl.g:330:2: (this_Float_0= ruleFloat )
            // InternalDsl.g:331:2: this_Float_0= ruleFloat
            {

            		newCompositeNode(grammarAccess.getAngleAccess().getFloatParserRuleCall());
            	
            pushFollow(FOLLOW_2);
            this_Float_0=ruleFloat();

            state._fsp--;


            		current.merge(this_Float_0);
            	

            		afterParserOrEnumRuleCall();
            	

            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAngle"


    // $ANTLR start "entryRuleTime"
    // InternalDsl.g:344:1: entryRuleTime returns [String current=null] : iv_ruleTime= ruleTime EOF ;
    public final String entryRuleTime() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleTime = null;


        try {
            // InternalDsl.g:344:44: (iv_ruleTime= ruleTime EOF )
            // InternalDsl.g:345:2: iv_ruleTime= ruleTime EOF
            {
             newCompositeNode(grammarAccess.getTimeRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleTime=ruleTime();

            state._fsp--;

             current =iv_ruleTime.getText(); 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleTime"


    // $ANTLR start "ruleTime"
    // InternalDsl.g:351:1: ruleTime returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_Float_0= ruleFloat this_UNITS_1= RULE_UNITS ) ;
    public final AntlrDatatypeRuleToken ruleTime() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_UNITS_1=null;
        AntlrDatatypeRuleToken this_Float_0 = null;



        	enterRule();

        try {
            // InternalDsl.g:357:2: ( (this_Float_0= ruleFloat this_UNITS_1= RULE_UNITS ) )
            // InternalDsl.g:358:2: (this_Float_0= ruleFloat this_UNITS_1= RULE_UNITS )
            {
            // InternalDsl.g:358:2: (this_Float_0= ruleFloat this_UNITS_1= RULE_UNITS )
            // InternalDsl.g:359:3: this_Float_0= ruleFloat this_UNITS_1= RULE_UNITS
            {

            			newCompositeNode(grammarAccess.getTimeAccess().getFloatParserRuleCall_0());
            		
            pushFollow(FOLLOW_10);
            this_Float_0=ruleFloat();

            state._fsp--;


            			current.merge(this_Float_0);
            		

            			afterParserOrEnumRuleCall();
            		
            this_UNITS_1=(Token)match(input,RULE_UNITS,FOLLOW_2); 

            			current.merge(this_UNITS_1);
            		

            			newLeafNode(this_UNITS_1, grammarAccess.getTimeAccess().getUNITSTerminalRuleCall_1());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleTime"


    // $ANTLR start "entryRuleFloat"
    // InternalDsl.g:380:1: entryRuleFloat returns [String current=null] : iv_ruleFloat= ruleFloat EOF ;
    public final String entryRuleFloat() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleFloat = null;


        try {
            // InternalDsl.g:380:45: (iv_ruleFloat= ruleFloat EOF )
            // InternalDsl.g:381:2: iv_ruleFloat= ruleFloat EOF
            {
             newCompositeNode(grammarAccess.getFloatRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleFloat=ruleFloat();

            state._fsp--;

             current =iv_ruleFloat.getText(); 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleFloat"


    // $ANTLR start "ruleFloat"
    // InternalDsl.g:387:1: ruleFloat returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : ( (this_NONZERO_DIGIT_0= RULE_NONZERO_DIGIT | this_ZERO_1= RULE_ZERO )+ (kw= ',' (this_NONZERO_DIGIT_3= RULE_NONZERO_DIGIT | this_ZERO_4= RULE_ZERO )+ )? ) ;
    public final AntlrDatatypeRuleToken ruleFloat() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_NONZERO_DIGIT_0=null;
        Token this_ZERO_1=null;
        Token kw=null;
        Token this_NONZERO_DIGIT_3=null;
        Token this_ZERO_4=null;


        	enterRule();

        try {
            // InternalDsl.g:393:2: ( ( (this_NONZERO_DIGIT_0= RULE_NONZERO_DIGIT | this_ZERO_1= RULE_ZERO )+ (kw= ',' (this_NONZERO_DIGIT_3= RULE_NONZERO_DIGIT | this_ZERO_4= RULE_ZERO )+ )? ) )
            // InternalDsl.g:394:2: ( (this_NONZERO_DIGIT_0= RULE_NONZERO_DIGIT | this_ZERO_1= RULE_ZERO )+ (kw= ',' (this_NONZERO_DIGIT_3= RULE_NONZERO_DIGIT | this_ZERO_4= RULE_ZERO )+ )? )
            {
            // InternalDsl.g:394:2: ( (this_NONZERO_DIGIT_0= RULE_NONZERO_DIGIT | this_ZERO_1= RULE_ZERO )+ (kw= ',' (this_NONZERO_DIGIT_3= RULE_NONZERO_DIGIT | this_ZERO_4= RULE_ZERO )+ )? )
            // InternalDsl.g:395:3: (this_NONZERO_DIGIT_0= RULE_NONZERO_DIGIT | this_ZERO_1= RULE_ZERO )+ (kw= ',' (this_NONZERO_DIGIT_3= RULE_NONZERO_DIGIT | this_ZERO_4= RULE_ZERO )+ )?
            {
            // InternalDsl.g:395:3: (this_NONZERO_DIGIT_0= RULE_NONZERO_DIGIT | this_ZERO_1= RULE_ZERO )+
            int cnt4=0;
            loop4:
            do {
                int alt4=3;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==RULE_NONZERO_DIGIT) ) {
                    alt4=1;
                }
                else if ( (LA4_0==RULE_ZERO) ) {
                    alt4=2;
                }


                switch (alt4) {
            	case 1 :
            	    // InternalDsl.g:396:4: this_NONZERO_DIGIT_0= RULE_NONZERO_DIGIT
            	    {
            	    this_NONZERO_DIGIT_0=(Token)match(input,RULE_NONZERO_DIGIT,FOLLOW_11); 

            	    				current.merge(this_NONZERO_DIGIT_0);
            	    			

            	    				newLeafNode(this_NONZERO_DIGIT_0, grammarAccess.getFloatAccess().getNONZERO_DIGITTerminalRuleCall_0_0());
            	    			

            	    }
            	    break;
            	case 2 :
            	    // InternalDsl.g:404:4: this_ZERO_1= RULE_ZERO
            	    {
            	    this_ZERO_1=(Token)match(input,RULE_ZERO,FOLLOW_11); 

            	    				current.merge(this_ZERO_1);
            	    			

            	    				newLeafNode(this_ZERO_1, grammarAccess.getFloatAccess().getZEROTerminalRuleCall_0_1());
            	    			

            	    }
            	    break;

            	default :
            	    if ( cnt4 >= 1 ) break loop4;
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        throw eee;
                }
                cnt4++;
            } while (true);

            // InternalDsl.g:412:3: (kw= ',' (this_NONZERO_DIGIT_3= RULE_NONZERO_DIGIT | this_ZERO_4= RULE_ZERO )+ )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==17) ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // InternalDsl.g:413:4: kw= ',' (this_NONZERO_DIGIT_3= RULE_NONZERO_DIGIT | this_ZERO_4= RULE_ZERO )+
                    {
                    kw=(Token)match(input,17,FOLLOW_7); 

                    				current.merge(kw);
                    				newLeafNode(kw, grammarAccess.getFloatAccess().getCommaKeyword_1_0());
                    			
                    // InternalDsl.g:418:4: (this_NONZERO_DIGIT_3= RULE_NONZERO_DIGIT | this_ZERO_4= RULE_ZERO )+
                    int cnt5=0;
                    loop5:
                    do {
                        int alt5=3;
                        int LA5_0 = input.LA(1);

                        if ( (LA5_0==RULE_NONZERO_DIGIT) ) {
                            alt5=1;
                        }
                        else if ( (LA5_0==RULE_ZERO) ) {
                            alt5=2;
                        }


                        switch (alt5) {
                    	case 1 :
                    	    // InternalDsl.g:419:5: this_NONZERO_DIGIT_3= RULE_NONZERO_DIGIT
                    	    {
                    	    this_NONZERO_DIGIT_3=(Token)match(input,RULE_NONZERO_DIGIT,FOLLOW_12); 

                    	    					current.merge(this_NONZERO_DIGIT_3);
                    	    				

                    	    					newLeafNode(this_NONZERO_DIGIT_3, grammarAccess.getFloatAccess().getNONZERO_DIGITTerminalRuleCall_1_1_0());
                    	    				

                    	    }
                    	    break;
                    	case 2 :
                    	    // InternalDsl.g:427:5: this_ZERO_4= RULE_ZERO
                    	    {
                    	    this_ZERO_4=(Token)match(input,RULE_ZERO,FOLLOW_12); 

                    	    					current.merge(this_ZERO_4);
                    	    				

                    	    					newLeafNode(this_ZERO_4, grammarAccess.getFloatAccess().getZEROTerminalRuleCall_1_1_1());
                    	    				

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt5 >= 1 ) break loop5;
                                EarlyExitException eee =
                                    new EarlyExitException(5, input);
                                throw eee;
                        }
                        cnt5++;
                    } while (true);


                    }
                    break;

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleFloat"

    // Delegated rules


 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x000000000000E002L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x0000000000000180L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_11 = new BitSet(new long[]{0x0000000000020182L});
    public static final BitSet FOLLOW_12 = new BitSet(new long[]{0x0000000000000182L});

}