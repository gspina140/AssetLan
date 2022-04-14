package ast;

import java.util.ArrayList;
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
	public Node program(programContext ctx) {

		// Resulting node of the right type
		ProgLetInNode res;
		
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
		FunNode res = new FunctionNode(visit(ctx.type()), ctx.ID().getText());
		
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
    public Node visitAssignment(AssignmentContext ctx){
        return new AssignmentNode(ctx.ID().getText(), visit(ctx.exp()));
    }

    @Override
    public Node visitDec(DecContext ctx){
        return new DecNode(visit(ctx.type()), ctx.ID().getText());
    }

    @Override
    public Node visitPrint(PrintContext ctx){
        return new PrintNode(visit(ctx.exp()));    
    }

    @Override
    public Node visitMove(MoveContext ctx){
        ArrayList<Integer> id = new ArrayList<Integer>();

        for(Integer ids : ctx.ID())
            id.add(ids);

        return new MoveNode(id.get(0).getText(), id.get(2).getText());
    }

    @Override
    public Node visitRet(RetContext ctx){
        return new ReturnNode(visit(ctx.exp()));
    }
    
    @Override
    public Node visitTransfer(TransferContext ctx){
        return new TransferNode(ctx.ID().getText());
    }

    public Node visitIte(IteContext ctx){
        IteNode res = new IteNode(visit(ctx.exp()));

        for(StatementContext sc: ctx.statement())
            res.addStatement(visit(sc));

        return res;
    }
}
