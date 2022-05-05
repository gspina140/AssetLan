package ast;

import java.util.ArrayList;

import org.antlr.v4.runtime.tree.TerminalNode;

import parser.AssetLanBaseVisitor;
import parser.AssetLanParser.AdecContext;
import parser.AssetLanParser.AssetContext;
import parser.AssetLanParser.AssignExpContext;
import parser.AssetLanParser.AssignmentContext;
import parser.AssetLanParser.BaseExpContext;
import parser.AssetLanParser.BinExpContext;
import parser.AssetLanParser.BoolExpContext;
import parser.AssetLanParser.CallContext;
import parser.AssetLanParser.CallExpContext;
import parser.AssetLanParser.CallFunContext;
import parser.AssetLanParser.DecContext;
import parser.AssetLanParser.DerExpContext;
import parser.AssetLanParser.ExpContext;
import parser.AssetLanParser.ExplistContext;
import parser.AssetLanParser.FieldContext;
import parser.AssetLanParser.FunctionContext;
import parser.AssetLanParser.InitcallContext;
import parser.AssetLanParser.IfElseExpContext;
import parser.AssetLanParser.IteContext;
import parser.AssetLanParser.MoveAssetContext;
import parser.AssetLanParser.MoveContext;
import parser.AssetLanParser.NegExpContext;
import parser.AssetLanParser.NotExpContext;
import parser.AssetLanParser.PrintContext;
import parser.AssetLanParser.PrintExpContext;
import parser.AssetLanParser.ProgramContext;
import parser.AssetLanParser.RetContext;
import parser.AssetLanParser.ReturnExpContext;
import parser.AssetLanParser.StatementContext;
import parser.AssetLanParser.TransferAssetContext;
import parser.AssetLanParser.TransferContext;
import parser.AssetLanParser.TypeContext;
import parser.AssetLanParser.ValExpContext;

import util.Environment;
import util.SemanticError;

public class AssetLanVisitorImpl extends AssetLanBaseVisitor<Node> {	

    /**
     * Override of the visit of a Program node
     * @param ctx the context of the visit, containing information about the node
     * @return the corresponding ProgramNode
     */
	@Override
	public Node visitProgram(ProgramContext ctx) {

		// List of fields in @res
		ArrayList<Node> fields = new ArrayList<Node>();

		// List of assets in @res
		ArrayList<Node> assets = new ArrayList<Node>();

		// List of functions in @res
		ArrayList<Node> functions = new ArrayList<Node>();

        // Node containing initcall in @res
        Node initcall = null;
		
		// Visit all nodes corresponding to fields inside the program context and store them in @fields
        for(FieldContext flc : ctx.field())
            fields.add(visit(flc));

		// Visit all nodes corresponding to assets inside the program context and store them in @assets
        for(AssetContext ac : ctx.asset())
            assets.add(visit(ac));

		// Visit all nodes corresponding to functions inside the program context and store them in @functions
        for(FunctionContext fnc : ctx.function())
            functions.add(visit(fnc));

		// Visit initcall context
        try {
            initcall = visitInitcall(ctx.initcall());
        } catch (NullPointerException e) {
            System.err.println("Warning: an error has occured when trying to visit initcall node\n");
        }

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
            return new FieldNode(visit(ctx.type()), ctx.ID().getText(), visit(ctx.exp()));
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

    /**
     * Override of the visit of a Function node
     * @param ctx the context of the visit, containing information about the node
     * @return the corresponding FunctionNode
     */
	@Override
	public Node visitFunction(FunctionContext ctx) {
		
        // Declare function node
        FunctionNode res;

        // Create new function node according to type (if 'void', pass null as type nodeto the constructor)
        if(ctx.type() != null) {
            res = new FunctionNode(visit(ctx.type()), ctx.ID().getText());
        } else{
            res = new FunctionNode(null, ctx.ID().getText());
        }
        
        // If there are parameter declarations, visit the declaration node containing them and add it to the function node
        if(ctx.par != null){
            res.addPar(visitDec(ctx.par));
        }

        // If there are asset declarations, visit the asset declaration node containing them and add it to the function node
        if(ctx.adec() != null){
            res.addAsset(visitAdec(ctx.adec()));
        }

        // If there are inner declarations, visit each declaration and add the respective node to the list in the function node
        // Remark: each declaration node can contain more than one variable declaration (each node correspond to a line of declarations)
        for(int i = 0; i < ctx.innerDec.size(); i++){
            res.addDec(visitDec(ctx.innerDec.get(i)));
        }
		
		// For each statement in the function body, visit it and add it to the list of statements in the function node
        for(StatementContext sc : ctx.statement())
            res.addStatement(visit(sc));
		
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

    /**
     * Override of the visit of an Assignment node
     * @param ctx the context of the visit, containing information about the node
     * @return the corresponding AssignmentNode
     */
    @Override
    public Node visitAssignExp(AssignExpContext ctx){
        return visitAssignment(ctx.assignment());
    }

    /**
     * Override of the visit of a Move Asset node
     * @param ctx the context of the visit, containing information about the node
     * @return the corresponding MoveAssetNode
     */
    @Override 
    public Node visitMoveAsset(MoveAssetContext ctx){
        return visitMove(ctx.move());
    }

    /**
     * Override of the visit of a Print Expression node
     * @param ctx the context of the visit, containing information about the node
     * @return the corresponding PrintExpNode
     */
    @Override
    public Node visitPrintExp(PrintExpContext ctx){
        return visitPrint(ctx.print());
    }

    /**
     * Override of the visit of a Transfer Asset node
     * @param ctx the context of the visit, containing information about the node
     * @return the corresponding TransferAssetNode
     */
    @Override
    public Node visitTransferAsset(TransferAssetContext ctx){
        return visitTransfer(ctx.transfer());
    }

    /**
     * Override of the visit of a Return Expression node
     * @param ctx the context of the visit, containing information about the node
     * @return the corresponding ReturnExpNode
     */
    @Override
    public Node visitReturnExp(ReturnExpContext ctx){
        return visitRet(ctx.ret());
    }

    /**
     * Override of the visit of an If-Then-Else expression node
     * @param ctx the context of the visit, containing information about the node
     * @return the corresponding IfElseExpNode
     */
    @Override
    public Node visitIfElseExp(IfElseExpContext ctx){
        return visitIte(ctx.ite());
    }

    /**
     * Override of the visit of a Call Function node
     * @param ctx the context of the visit, containing information about the node
     * @return the corresponding CallFunNode
     */
    @Override
    public Node visitCallFun(CallFunContext ctx){
        return visitCall(ctx.call());
    }

    /**
     * Override of the visit of a Type node
     * @param ctx the context of the visit, containing information about the node
     * @return the corresponding TypeNode
     */
    @Override
    public Node visitType(TypeContext ctx){

        // Check if type is integer or boolean
        if(ctx.getText().equals("int"))
            return new IntTypeNode();
        else if(ctx.getText().equals("bool"))
            return new BoolTypeNode();
        
        return null;
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

    /**
     * Override of the visit of a Move node
     * @param ctx the context of the visit, containing information about the node
     * @return the corresponding MoveNode
     */
    @Override
    public Node visitMove(MoveContext ctx){
        String fOp = ctx.ID().get(0).getText(); // Get from context the id of the assets to move
        String sOp = ctx.ID().get(1).getText();

        return new MoveNode(fOp, sOp);
    }

    /**
     * Override of the visit of a Print node
     * @param ctx the context of the visit, containing information about the node
     * @return the corresponding PrintNode
     */
    @Override
    public Node visitPrint(PrintContext ctx){
        return new PrintNode(visit(ctx.exp()));    
    }
    
    /**
     * Override of the visit of a Transfer node
     * @param ctx the context of the visit, containing information about the node
     * @return the corresponding TransferNode
     */
    @Override
    public Node visitTransfer(TransferContext ctx){
        return new TransferNode(ctx.ID().getText());
    }

    /**
     * Override of the visit of a Return node
     * @param ctx the context of the visit, containing information about the node
     * @return the corresponding ReturnNode
     */
    @Override
    public Node visitRet(RetContext ctx){
        if(ctx.exp() != null)
            return new ReturnNode(visit(ctx.exp()));
        else
            return new ReturnNode(null);
    }

    /**
     * Override of the visit of an If-Then-Else node
     * @param ctx the context of the visit, containing information about the node
     * @return the corresponding IteNode
     */
    @Override
    public Node visitIte(IteContext ctx){

        // Create the resulting node passing the expression node containing the condition
        IteNode res = new IteNode(visit(ctx.exp()));

        // For each statement in the 'then' and 'else' blocks, add it to the statementList
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
        CallNode res = new CallNode(ids.get(0), visitExplist(ctx.explist()));

        // Add assets id to the node
        for(int i=1; i < ids.size(); i++)
            res.addId(ids.get(i));

        return res;
    }

    /**
     * Override of the visit of a InitCall node
     * @param ctx the context of the visit, containing information about the node
     * @return the corresponding InitCallNode
     */
    @Override
    public Node visitInitcall(InitcallContext ctx){

        InitCallNode res = new InitCallNode(ctx.ID().getText());

        if(ctx.parameters != null)
            res.addPar(visitExplist(ctx.parameters));

        if(ctx.assets != null)
            res.addAs(visitExplist(ctx.assets));

        return res;
    }

    @Override
    public Node visitExplist(ExplistContext ctx){
        ArrayList<Node> exps = new ArrayList<>();

        for(ExpContext ec : ctx.exp()){
            exps.add(visit(ec));
        }

        ExpListNode res = new ExpListNode(exps.get(0));

        for(int i=1; i<exps.size(); i++)
            res.addExp(exps.get(i));

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
        String s =ctx.op.getText();

        switch(s){
            case "+":
                return new PlusMinusNode(visit(ctx.exp(0)), visit(ctx.exp(1)));
            case "-":
                return new PlusMinusNode(visit(ctx.exp(0)), visit(ctx.exp(1)));
            case "*":
                return new MultDivNode(visit(ctx.exp(0)), visit(ctx.exp(1)));
            case "/":
                return new MultDivNode(visit(ctx.exp(0)), visit(ctx.exp(1)));
            case "==":
                return new EqualDiffNode(visit(ctx.exp(0)), visit(ctx.exp(1)));
            case "!=":
                return new EqualDiffNode(visit(ctx.exp(0)), visit(ctx.exp(1)));
            case "<":
                return new MinorMajorNode(visit(ctx.exp(0)), visit(ctx.exp(1)));
            case "<=":
                return new MinorMajorNode(visit(ctx.exp(0)), visit(ctx.exp(1)));
            case ">":
                return new MinorMajorNode(visit(ctx.exp(0)), visit(ctx.exp(1)));
            case ">=":
                return new MinorMajorNode(visit(ctx.exp(0)), visit(ctx.exp(1)));
            case "&&":
                return new LogicOpNode(visit(ctx.exp(0)), visit(ctx.exp(1)));
            case "||":
                return new LogicOpNode(visit(ctx.exp(0)), visit(ctx.exp(1)));
            default:
                return null;
        }
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

    /**
     * Override of the visit of a Value Expression node
     * @param ctx the context of the visit, containing information about the node
     * @return the corresponding ValExpNode
     */
    @Override
    public Node visitValExp(ValExpContext ctx){
        try{
            return new ValExpNode(Integer.parseInt(ctx.NUMBER().getText()));
        }catch (NumberFormatException e){   // The number exceeds the dimension of an integer
            System.err.println("Warning: dimension of NUMBER token is too long\n");
            return null;
        }
    }

    /**
     * Override of the visit of a Negative Expression node
     * @param ctx the context of the visit, containing information about the node
     * @return the corresponding NegExpNode
     */
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

    /**
     * Override of the visit of a Call Expression node
     * @param ctx the context of the visit, containing information about the node
     * @return the corresponding CallExpNode
     */
    @Override
    public Node visitCallExp(CallExpContext ctx){
        return visitCall(ctx.call());
    }

    /**
     * Override of the visit of a Not Expression node
     * @param ctx the context of the visit, containing information about the node
     * @return the corresponding NotExpNode
     */
    @Override
    public Node visitNotExp(NotExpContext ctx){
        return new BaseExpNode(visit(ctx.exp()));
    }
}
