// Generated from Proyecto.g4 by ANTLR 4.8
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ProyectoParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ProyectoVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link ProyectoParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(ProyectoParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link ProyectoParser#classDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassDeclaration(ProyectoParser.ClassDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ProyectoParser#declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaration(ProyectoParser.DeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ProyectoParser#varDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDeclaration(ProyectoParser.VarDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ProyectoParser#structDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructDeclaration(ProyectoParser.StructDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ProyectoParser#varType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarType(ProyectoParser.VarTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ProyectoParser#methodDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodDeclaration(ProyectoParser.MethodDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ProyectoParser#methodType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodType(ProyectoParser.MethodTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ProyectoParser#parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameter(ProyectoParser.ParameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link ProyectoParser#parameterType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterType(ProyectoParser.ParameterTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ProyectoParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(ProyectoParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link ProyectoParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(ProyectoParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ProyectoParser#location}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocation(ProyectoParser.LocationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ProyectoParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(ProyectoParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ProyectoParser#methodCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodCall(ProyectoParser.MethodCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link ProyectoParser#op}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOp(ProyectoParser.OpContext ctx);
	/**
	 * Visit a parse tree produced by {@link ProyectoParser#arithOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArithOp(ProyectoParser.ArithOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link ProyectoParser#relOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelOp(ProyectoParser.RelOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link ProyectoParser#eqOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqOp(ProyectoParser.EqOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link ProyectoParser#condOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCondOp(ProyectoParser.CondOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link ProyectoParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(ProyectoParser.LiteralContext ctx);
}