package ast;

import java.util.ArrayList;

import org.w3c.dom.Node;

import AssetLanParser.AssignExpContext;
import AssetLanParser.CallFunContext;
import AssetLanParser.IfElseExpContext;
import AssetLanParser.MoveAssetContext;
import AssetLanParser.PrintExpContext;
import AssetLanParser.ReturnExpContext;
import AssetLanParser.TransferAssetContext;
import parser.*;
import parser.AssetLanParser.AssetContext;
import parser.AssetLanParser.AssignmentContext;
import parser.AssetLanParser.BaseExpContext;
import parser.AssetLanParser.BinExpContext;
import parser.AssetLanParser.BoolExpContext;
import parser.AssetLanParser.CallContext;
import parser.AssetLanParser.CallExpContext;
import parser.AssetLanParser.DecContext;
import parser.AssetLanParser.DerExpContext;
import parser.AssetLanParser.ExpContext;
import parser.AssetLanParser.FieldContext;
import parser.AssetLanParser.FunctionContext;
import parser.AssetLanParser.InitcallContext;
import parser.AssetLanParser.IteContext;
import parser.AssetLanParser.MoveContext;
import parser.AssetLanParser.NegExpContext;
import parser.AssetLanParser.NotExpContext;
import parser.AssetLanParser.PrintContext;
import parser.AssetLanParser.ProgramContext;
import parser.AssetLanParser.RetContext;
import parser.AssetLanParser.StatementContext;
import parser.AssetLanParser.TransferContext;
import parser.AssetLanParser.TypeContext;
import parser.AssetLanParser.ValExpContext;

public class AssetLanVisitorImpl extends AssetLanBaseVisitor<Node> {
	

	@Override
	public Node visitProgram(ProgramContext ctx) {

		// Resulting node of the right type
		ProgamNode res;
		
		// List of fields in @res
		ArrayList<Node> fields = new ArrayList<Node>();

		// List of assets in @res
		ArrayList<Node> assets = new ArrayList<Node>();

		// List of functions in @res
		ArrayList<Node> functions = new ArrayList<Node>();
		
		/* Visit all nodes corresponding to fields, assets and functions inside the program context
		 * and store them in @fields, @assets and @functions rexpectively;
		 * Notice that, for example, the ctx.let().field() method returns a list, this is because of
		 * the use of * or + in the grammar antlr detects this is a group and therefore returns a list */
		for (FieldContext fldc : ctx.let().field()){
			fields.add( visit(fldc) );	// We used fldc instead of fc to do not confuse field with function
		}

		for (AssetContext ac : ctx.let().asset()){
			assets.add( visit(ac) );
		}

		for (FunctionContext func : ctx.let().function()){
			functions.add( visit(func) );
		}
		
		// Visit initcall context
		Node initcall = visit( ctx.initcall() );
		
		// Build @res accordingly with the result of the visits to its content
		res = new ProgramNode(fields, assets, functions, initcall);
		
		return res;
	}

	@Override
	public Node visitField(FieldContext ctx) {
		// Visit the type
		Node typeNode = visit(ctx.type());
		
		// Visit the exp
		Node expNode = visit(ctx.exp());
		
		// Build the FieldNode
		return new FieldNode(typeNode, ctx.ID().getText(), expNode);
	}

	@Override
	public Node visitAsset(AssetContext ctx) {
		return new AssetNode(ctx.ID().getText());
	}

	@Override
	public Node visitFunction(FunctionContext ctx) {
		
		// Initialize @res with the visits to the type and its ID
		FunctionNode res = new FunctionNode(visit(ctx.type()), ctx.ID().getText());
		
		// Add argument declarations
		// We are getting a shortcut here by constructing directly the ParNode
		// This could be done differently by visiting instead the DecContext
		// SUPPOSITION: this contains both parameters and inner declarations!
		// (This is because of the shared decContext)
		for(DecContext dc : ctx.dec())
			res.addPar( new ParNode(dc.ID().getText(), visit( dc.type() )) );

		// Add assets declarations
		for(AssetContext ac : ctx.asset())
			res.addAsset( new AssetNode(ac.ID().getText()));
		
		// Add body
		// Nested declarations should already be considered

		// Create a list for the statements
		ArrayList<Node> statementlist = new ArrayList<Node>();
		
		// Check whether there are actually statements
		if(ctx.statement() != null){
			// If there are visit each statement and add it to the @statement list
			for(StatementContext sc : ctx.statement())
				statementlist.add(visit(sc));
		}
		
		// Add the body (e.g., the statementlist) to the function
		res.addBody(statementlist);
		
		return res;		
		
	}

    @Override
    public Node visitDec(DecContext ctx){
        return new DecNode(visit(ctx.type()), ctx.ID().getText());
    }

    @Override
    public Node visitADec(AdecContext ctx){
        ADecNode res = new ADecNode();

        for(AdecContext ac: ctx.ID()){
            res.addId(ac.getText());
        }

        return res;
    }

    @Override
    public Node visitAssignExp(AssignExpContext ctx){
        return new AssignmentNode(ctx.ID().getText(), visit(ctx.exp()));
    }

    @Override 
    public Node visitMoveAsset(MoveAssetContext ctx){
        String fOp = ctx.ID().get(0).getText();
        String sOp = ctx.ID().get(1).getText();

        return new MoveNode(fOp, sOp);
    }

    @Override
    public Node visitPrintExp(PrintExpContext ctx){
        return new PrintNode(visit(ctx.exp()));    
    }

    @Override
    public Node visitTransferAsset(TransferAssetContext ctx){
        return new TransferNode(ctx.ID().getText());
    }

    @Override
    public Node visitReturnExp(ReturnExpContext ctx){
        return new ReturnNode(visit(ctx.exp()));
    }

    @Override
    public Node visitIfElseExp(IfElseExpContext ctx){
        IteNode res = new IteNode(visit(ctx.exp()));

        for(StatementContext sc: ctx.statement())
            res.addStatement(visit(sc));

        return res;
    }

    @Override
    public Node visitCallFun(CallFunContext ctx){/*
        ArrayList<String> ids = new ArrayList<String>();

        for(String id: ctx.ID().getText())
            ids.add(id);

        CallNode res = new CallNode(ids.get(0));
        
        for(int i=1; i < ids.size(); i++)
            res.addId(ids.get(i));

        for(ExpContext ec: ctx.exp())
            res.addExp(visit(ec));


        return res;*/
        return visitCall(ctx.call());
    }
/* type node TODO
    @Override
    public Node visitType(TypeContext ctx){

    }
*/

/*  Since labels were added for all subrules of "statement" i think that the 
    following methods are useless (they are just a redefinition of the previous one).
    Maybe for "visitCallFun" there must be another implementation because this is the 
    only subrule of "statement" that is mentioned as subrule in another rule (exp),
    so it must be implemented again, in a completely equal way, just changing the 
    method name with the label name, in this case will be visitCallExp.
    I think that it would be possible to declare this function only one time,
    and so reduce redudancy in code, just by giving the same label at both
    the subrules where it appears. I'm not sure that this is legit anyway!!
*/  
    @Override
    public Node visitAssignment(AssignmentContext ctx){
        return new AssignmentNode(ctx.ID().getText(), visit(ctx.exp()));
    }

    @Override
    public Node visitMove(MoveContext ctx){
        String fOp = ctx.ID().get(0).getText();
        String sOp = ctx.ID().get(1).getText();

        return new MoveNode(fOp, sOp);
    }

    @Override
    public Node visitPrint(PrintContext ctx){
        return new PrintNode(visit(ctx.exp()));    
    }
    
    @Override
    public Node visitTransfer(TransferContext ctx){
        return new TransferNode(ctx.ID().getText());
    }

    @Override
    public Node visitRet(RetContext ctx){
        return new ReturnNode(visit(ctx.exp()));
    }

    @Override
    public Node visitIte(IteContext ctx){
        IteNode res = new IteNode(visit(ctx.exp()));

        for(StatementContext sc: ctx.statement())
            res.addStatement(visit(sc));

        return res;
    }

    @Override
    public Node visitCall(CallContext ctx){
        ArrayList<String> ids = new ArrayList<String>();

        for(String id: ctx.ID().getText())
            ids.add(id);

        CallNode res = new CallNode(ids.get(0));
        
        for(int i=1; i < ids.size(); i++)
            res.addId(ids.get(i));

        for(ExpContext ec: ctx.exp())
            res.addExp(visit(ec));


        return res;
    }

    @Override
    public Node visitInitcall(InitcallContext ctx){
        InitCallNode res = new InitCallNode(visit(ctx.ID().getText()));

        for(ExpContext ec:ctx.exp())
            res.addExp(visit(ec));

        return res;
    }

    @Override
    public Node visitBaseExp(BaseExpContext ctx){
        return new BaseExpNode(visit(ctx.exp()));
    }

    @Override
    public Node visitBinExp(BinExpContext ctx){
        return new BinExpNode(visit(ctx.exp(0)), visit(ctx.exp(1)));
    }

    @Override
    public Node visitDerExp(DerExpContext ctx){
        return new DerExpNode(ctx.ID().getText());
    }

    @Override
    public Node visitValExp(ValExpContext ctx){
        return new ValExpNode(Integer.parseInt(ctx.INTEGER().getText()));
    }

    @Override
    public Node visitNegExp(NegExpContext ctx){
        return new BaseExpNode(visit(ctx.exp()));
    }

    @Override 
    public Node visitBoolExp(BoolExpContext ctx){
		return new BoolExpNode(Boolean.parseBoolean(ctx.getText())); 
    }

    @Override
    public Node visitCallExp(CallExpContext ctx){
        return visitCall(ctx.call());
    }

    @Override
    public Node visitNotExp(NotExpContext ctx){
        return new BaseExpNode(visit(ctx.exp()));
    }
}
