package com.lonicera.expr;

import com.lonicera.bnf.BNF;
import com.lonicera.node.Node;
import com.lonicera.token.Token;

/**
 * @author LiBowei
 * @date 2020年-11月-25日
 **/
public class JsonExpr implements Expr, BNF<Expr> {

  public void match() {
    or(new ObjectExpr(), new ArrayExpr());
  }

  public Node<Token> make() {
    return null;
  }

  public void option(Expr expr) {

  }

  public void repeat(Expr expr) {

  }

  public void or(Expr... exprs) {

  }
}
