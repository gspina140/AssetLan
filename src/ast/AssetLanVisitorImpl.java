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
}
