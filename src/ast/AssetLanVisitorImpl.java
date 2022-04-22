package ast;

import java.util.ArrayList;

import org.antlr.v4.runtime.tree.TerminalNode;
import parser.AssetLanParser.AdecContext;
import parser.AssetLanParser.AssignExpContext;
import parser.AssetLanParser.CallFunContext;
import parser.AssetLanParser.IfElseExpContext;
import parser.AssetLanParser.MoveAssetContext;
import parser.AssetLanParser.PrintExpContext;
import parser.AssetLanParser.ReturnExpContext;
import parser.AssetLanParser.TransferAssetContext;
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
import util.Environment;
import util.SemanticError;

public class AssetLanVisitorImpl extends AssetLanBaseVisitor<Node> {
	

	@Override
	public Node visitProgram(ProgramContext ctx) {

		// Resulting node of the right type
		//ProgamNode res;
		
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

        for(FieldContext flc : ctx.field())
            fields.add(visit(flc));

        for(AssetContext ac : ctx.asset())
            assets.add(visit(ac));

        for(FunctionContext fnc : ctx.function())
            functions.add(visit(fnc));

		// Visit initcall context
        Node initcall = null;
        if(ctx.initcall().ID() != null)
		    initcall = visit( ctx.initcall() );

		// Build @res accordingly with the result of the visits to its content
		return new ProgramNode(fields, assets, functions, initcall);
	}

    /**
     * Override of the visit of a Field node
     * @param ctx the context of the visit, containing information about the node
     * @return the corresponding FieldNode
     */
	@Override
	public Node visitField(FieldContext ctx) {

		// Check if there is an expression and use the constructor accordingly
        if(ctx.exp() != null) {
            return new FieldNode(visit(ctx.type()), ctx.ID().getText(), ctx.exp());
        } else {
            return new FieldNode(visit(ctx.type()), ctx.ID().getText(), null);
        }
        
		
	}

    /**
     * Override of the visit of an Asset node
     * @param ctx the context of the visit, containing information about the node
     * @return the corresponding AssetNode
     */
	@Override
	public Node visitAsset(AssetContext ctx) {
		return new AssetNode(ctx.ID().getText());
	}

	@Override
	public Node visitFunction(FunctionContext ctx) {
		
        FunctionNode res;

        if(ctx.type() != null) {
            res = new FunctionNode(visit(ctx.type()), ctx.ID().getText());
        }else{
            Node type = null;
            res = new FunctionNode(type, ctx.ID().getText());
        }
		// Initialize @res with the visits to the type and its ID
	//	FunctionNode res = new FunctionNode(visit(ctx.type()), ctx.ID().getText());
		
		// Add argument declarations
		// We are getting a shortcut here by constructing directly the ParNode
		// This could be done differently by visiting instead the DecContext
		// SUPPOSITION: this contains both parameters and inner declarations!
		// (This is because of the shared decContext)
        int parNum = ctx.par.size();
        DecNode par;
        //If i'm not wrong the '?' operator in the rule means optional argument, so if is present is one
        if(parNum > 0){
            par = (DecNode) visitDec(ctx.par.get(0));//new DecNode(ctx.par.get(0).type(0), ctx.par.get(0).ID(0).getText());
            res.addPar(par);
        }

        AdecNode as;
        if(ctx.adec() != null){ //There is an asset declaration
            as = (AdecNode) visitAdec(ctx.adec());
            res.addAsset(as);
        }

        // Add body
		// Nested declarations should already be considered
        int decNum = ctx.innerDec.size();
        //ArrayList<Node> decs = new ArrayList<Node>();

        //There is a kleene star in the rule, declarations can be 0 or more
        if(decNum > 0){
            DecNode dec = (DecNode) visitDec(ctx.innerDec.get(0)); //new DecNode(ctx.innerDec.get(0).type(0), ctx.innerDec.get(0).ID(0).getText());
            res.addDec(dec);

            for(int i=1; i < decNum; i++){
                dec = (DecNode) visitDec(ctx.innerDec.get(i));
                res.addDec(dec);
            }

        }
        
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

    /**
     * Override of the visit of a Dec node
     * All the declarations in the scope are managed in the same node
     * (i.e., we do not create a node for each asset declaration)
     * @param ctx the context of the visit, containing information about the node
     * @return the corresponding DecNode
     */
    @Override
    public Node visitDec(DecContext ctx){

        // List of types of new declarations
        ArrayList<Node> typeList  = new ArrayList<Node>();

        // List of ids of new declarations
        ArrayList<String> idList = new ArrayList<String>();

        // Population of typeList and idList
        for(int i=0; i < ctx.type().size(); i++){
            typeList.add(visit(ctx.type(i)));
            idList.add(ctx.ID(i).getText());
        }

        // Creation of resulting node with type and id of the first declaration (which is always present)
        DecNode res = new DecNode(typeList.get(0), idList.get(0));

        // If there are more than one declaration, add respective types and ids to the lists
        for(int i = 1; i < typeList.size(); i++)
            res.addDeclaration(typeList.get(i), idList.get(i));
        
        return res;
    }

    /**
     * Override of the visit of an Adec node
     * All the assets declared in the scope are managed in the same node
     * (i.e., we do not create a node for each asset declaration)
     * @param ctx the context of the visit, containing information about the node
     * @return the corresponding AdecNode
     */
    @Override
    public Node visitAdec(AdecContext ctx){

        // List of ids of the newly declared assets
        ArrayList<String> idList = new ArrayList<String>();

        // Population of the list
        for(int i=0; i < ctx.ID().size(); i++)
            idList.add(ctx.ID(i).getText());

        // Create a new Adec node passing the first id as an argument (at least 1 id id always declared)
        AdecNode res = new AdecNode(idList.get(0));
        
        // If there are other assets declarations, add them to the list
        for(int i=1; i < idList.size(); i++)
            res.addId(idList.get(i));

        return res;
    }

    @Override
    public Node visitAssignExp(AssignExpContext ctx){
        return visitAssignment(ctx.assignment());
    }

    @Override 
    public Node visitMoveAsset(MoveAssetContext ctx){
        return visitMove(ctx.move());
        /*String fOp = ctx.ID().get(0).getText();
        String sOp = ctx.ID().get(1).getText();

        return new MoveNode(fOp, sOp);*/
    }

    @Override
    public Node visitPrintExp(PrintExpContext ctx){
        return visitPrint(ctx.print());
    }

    @Override
    public Node visitTransferAsset(TransferAssetContext ctx){
        return visitTransfer(ctx.transfer());
    }

    @Override
    public Node visitReturnExp(ReturnExpContext ctx){
        return visitRet(ctx.ret());
    }

    @Override
    public Node visitIfElseExp(IfElseExpContext ctx){
        return visitIte(ctx.ite());
        /*IteNode res = new IteNode(visit(ctx.exp()));

        for(StatementContext sc: ctx.statement())
            res.addStatement(visit(sc));

        return res;*/
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

    /**
     * Override of the visit of an Assignment node
     * @param ctx the context of the visit, containing information about the node
     * @return the corresponding AssignmentNode
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
        if(ctx.exp() != null)
            return new ReturnNode(visit(ctx.exp()));
        else
            return new ReturnNode(null);
    }

    @Override
    public Node visitIte(IteContext ctx){
        IteNode res = new IteNode(visit(ctx.exp()));

        for(StatementContext sc: ctx.statement())
            res.addStatement(visit(sc));

        return res;
    }

    /**
     * Override of the visit of a Call node
     * @param ctx the context of the visit, containing information about the node
     * @return the corresponding CallNode
     */
    @Override
    public Node visitCall(CallContext ctx){

        // Temporary list containing the id of the function and the ids of the function assets
        ArrayList<String> ids = new ArrayList<String>();

        // Populate the temporary list
        for(int i=0; i < ctx.ID().size(); i++)
            ids.add(ctx.ID(i).getText());

        // Create the result node assigning the function id (first id in the tmeporary list)
        CallNode res = new CallNode(ids.get(0));

        // Add espressions defining the parameters to the node
        for(ExpContext ec: ctx.exp())
            res.addExp(visit(ec));
        
        // Add assets id to the node
        for(int i=1; i < ids.size(); i++)
            res.addId(ids.get(i));

        return res;
    }

    @Override
    public Node visitInitcall(InitcallContext ctx){
        InitCallNode res = new InitCallNode(ctx.ID().getText());

        for(ExpContext ec:ctx.exp())
            res.addExp(visit(ec));

        return res;
    }

    /**
     * Override of the visit of a Base Expression node
     * @param ctx the context of the visit, containing information about the node
     * @return the corresponding BaseExpNode
     */
    @Override
    public Node visitBaseExp(BaseExpContext ctx){
        return new BaseExpNode(visit(ctx.exp()));
    }

    /**
     * Override of the visit of a Binary Expression node
     * @param ctx the context of the visit, containing information about the node
     * @return the corresponding BinExpNode
     */
    @Override
    public Node visitBinExp(BinExpContext ctx){
        return new BinExpNode(visit(ctx.exp(0)), visit(ctx.exp(1)));
    }

    /**
     * Override of the visit of a Derivative Expression node
     * @param ctx the context of the visit, containing information about the node
     * @return the corresponding DerExpNode
     */
    @Override
    public Node visitDerExp(DerExpContext ctx){
        return new DerExpNode(ctx.ID().getText());
    }

    @Override
    public Node visitValExp(ValExpContext ctx){
        return new ValExpNode(Integer.parseInt(ctx.NUMBER().getText()));
    }

    @Override
    public Node visitNegExp(NegExpContext ctx){
        return new BaseExpNode(visit(ctx.exp()));
    }

    /**
     * Override of the visit of a Boolean Expression node
     * @param ctx the context of the visit, containing information about the node
     * @return the corresponding BoolExpNode
     */
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
