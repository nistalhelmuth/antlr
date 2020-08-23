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
	 * Visit a parse tree produced by {@link ProyectoParser#declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaration(ProyectoParser.DeclarationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code commonVarDeclaration}
	 * labeled alternative in {@link ProyectoParser#varDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommonVarDeclaration(ProyectoParser.CommonVarDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrayVarDeclaration}
	 * labeled alternative in {@link ProyectoParser#varDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayVarDeclaration(ProyectoParser.ArrayVarDeclarationContext ctx);
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
	 * Visit a parse tree produced by the {@code commonParameter}
	 * labeled alternative in {@link ProyectoParser#parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommonParameter(ProyectoParser.CommonParameterContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrayParameter}
	 * labeled alternative in {@link ProyectoParser#parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayParameter(ProyectoParser.ArrayParameterContext ctx);
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
	 * Visit a parse tree produced by the {@code ifStatement}
	 * labeled alternative in {@link ProyectoParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStatement(ProyectoParser.IfStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code whileStatement}
	 * labeled alternative in {@link ProyectoParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStatement(ProyectoParser.WhileStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code returnStatement}
	 * labeled alternative in {@link ProyectoParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnStatement(ProyectoParser.ReturnStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code methodCallStatement}
	 * labeled alternative in {@link ProyectoParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodCallStatement(ProyectoParser.MethodCallStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code blockStatement}
	 * labeled alternative in {@link ProyectoParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockStatement(ProyectoParser.BlockStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code locationStatement}
	 * labeled alternative in {@link ProyectoParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocationStatement(ProyectoParser.LocationStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionStatement}
	 * labeled alternative in {@link ProyectoParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionStatement(ProyectoParser.ExpressionStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ProyectoParser#location}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocation(ProyectoParser.LocationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionLiteral}
	 * labeled alternative in {@link ProyectoParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionLiteral(ProyectoParser.ExpressionLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionNegative}
	 * labeled alternative in {@link ProyectoParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionNegative(ProyectoParser.ExpressionNegativeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionNot}
	 * labeled alternative in {@link ProyectoParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionNot(ProyectoParser.ExpressionNotContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionLocation}
	 * labeled alternative in {@link ProyectoParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionLocation(ProyectoParser.ExpressionLocationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionGroup}
	 * labeled alternative in {@link ProyectoParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionGroup(ProyectoParser.ExpressionGroupContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionMethodCall}
	 * labeled alternative in {@link ProyectoParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionMethodCall(ProyectoParser.ExpressionMethodCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionCommon}
	 * labeled alternative in {@link ProyectoParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionCommon(ProyectoParser.ExpressionCommonContext ctx);
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
	 * Visit a parse tree produced by the {@code intLiteral}
	 * labeled alternative in {@link ProyectoParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntLiteral(ProyectoParser.IntLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code charLiteral}
	 * labeled alternative in {@link ProyectoParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCharLiteral(ProyectoParser.CharLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code boolLiteral}
	 * labeled alternative in {@link ProyectoParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolLiteral(ProyectoParser.BoolLiteralContext ctx);
}