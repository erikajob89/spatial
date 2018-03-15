package spade.node

import forge.tags._
import core._
import spatial.lang._
import pir.lang._

class PCUSpec(
  val nRegs   : Int, // Number of registers per stage
  val nCtrs   : Int, // Number of counters
  val nLanes  : Int, // Number of vector lanes
  val nStages : Int, // Number of stages
  val cIns    : List[Direction], // Control input directions
  val cOuts   : List[Direction], // Control output directions
  val sIns    : List[Direction], // Scalar input directions
  val sOuts   : List[Direction], // Scalar output directions
  val vIns    : List[Direction], // Vector input directions
  val vOuts   : List[Direction]  // Vector output directions
) extends PUSpec

@ref class PCU extends Box[PCU]

object PCU {
  @api def apply(spec: PCUSpec)(implicit wSize: Vec[Bit]): PCU = {
    implicit val vSize: Vec[Vec[Bit]] = Vec.bits[Vec[Bit]](spec.nLanes)

    val cIns  = Seq.fill(spec.nCIns){ bound[In[Bit]] }
    val cOuts = Seq.fill(spec.nCOuts){ bound[Out[Bit]] }
    val sIns  = Seq.fill(spec.nSIns){ bound[In[Vec[Bit]]] }
    val sOuts = Seq.fill(spec.nSOuts){ bound[Out[Vec[Bit]]] }
    val vIns  = Seq.fill(spec.nVIns){ bound[In[Vec[Vec[Bit]]]] }
    val vOuts = Seq.fill(spec.nVOuts){ bound[Out[Vec[Vec[Bit]]]] }

    stage(PCUModule(cIns,cOuts,sIns,sOuts,vIns,vOuts,spec))
  }
}

@op case class PCUModule(
  cIns:   Seq[In[Bit]],
  cOuts:  Seq[Out[Bit]],
  sIns:   Seq[In[Vec[Bit]]],
  sOuts:  Seq[Out[Vec[Bit]]],
  vIns:   Seq[In[Vec[Vec[Bit]]]],
  vOuts:  Seq[Out[Vec[Vec[Bit]]]],
  spec:   PCUSpec
) extends PUModule[PCU]