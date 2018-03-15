package spatial.node

import forge.tags._
import core._
import spatial.lang._

@op case class VecAlloc[T](elems: Seq[T])(implicit val tV: Vec[T]) extends Op[Vec[T]]
@op case class VecApply[T:Bits](vec: Vec[T], i: Int) extends Op[T]
@op case class VecSlice[T:Bits](vec: Vec[T], msw: Int, lsw: Int)(implicit val tV: Vec[T]) extends Op[Vec[T]]

@op case class VecConcat[T:Bits](vecs: Seq[Vec[T]])(implicit val tV: Vec[T]) extends Op[Vec[T]]

@op case class VecReverse[T:Bits](vec: Vec[T])(implicit val tV: Vec[T]) extends Op[Vec[T]]