package spatial.dse

import argon._
import spatial.metadata._
import poly.ISL
import spatial.targets._
import spatial.metadata.params._
import spatial.metadata.bounds._
import spatial.traversal._
import java.util.concurrent.BlockingQueue
import spatial.util.spatialConfig
import models._

case class DSEThread(
  threadId:  Int,
  params:    Seq[Sym[_]],
  space:     Seq[Domain[_]],
  accel:     Sym[_],
  program:   Block[_],
  localMems: Seq[Sym[_]],
  workQueue: BlockingQueue[Seq[BigInt]],
  outQueue:  BlockingQueue[Array[String]]
)(implicit val state: State, val isl: ISL) extends Runnable { thread =>
  // --- Thread stuff
  private var isAlive: Boolean = true
  private var hasTerminated: Boolean = false
  def requestStop(): Unit = { isAlive = false }
  var START: Long = 0

  // --- Profiling
  private final val PROFILING = false
  private var clockRef = 0L
  private def resetClock(): Unit = { clockRef = System.currentTimeMillis }

  var bndTime = 0L
  var memTime = 0L
  var conTime = 0L
  var areaTime = 0L
  var cyclTime = 0L

  private def endBnd(): Unit = { bndTime += System.currentTimeMillis - clockRef; resetClock() }
  private def endMem(): Unit = { memTime += System.currentTimeMillis - clockRef; resetClock() }
  private def endCon(): Unit = { conTime += System.currentTimeMillis - clockRef; resetClock() }
  private def endArea(): Unit = { areaTime += System.currentTimeMillis - clockRef; resetClock() }
  private def endCycles(): Unit = { cyclTime += System.currentTimeMillis - clockRef; resetClock() }
  private def resetAllTimers(): Unit = { memTime = 0; bndTime = 0; conTime = 0; areaTime = 0; cyclTime = 0 }

  // --- Space Stuff

  private val target = spatialConfig.target
  private val capacity: Area = target.capacity
  val areaHeading: Seq[String] = capacity.nonZeroFields
  private val indexedSpace = space.zipWithIndex
  private val N = space.length
  private val dims = space.map{d => BigInt(d.len) }
  private val prods = List.tabulate(N){i => dims.slice(i+1,N).product }

  private lazy val scalarAnalyzer = new ScalarAnalyzer(state)
  private lazy val memoryAnalyzer = new MemoryAnalyzer(state)

  private lazy val contentionAnalyzer = new ContentionAnalyzer(state)
  private lazy val areaAnalyzer  = spatialConfig.target.areaAnalyzer(state)
  // private lazy val cycleAnalyzer = target.cycleAnalyzer(state)

  // def init(): Unit = {
  //   areaAnalyzer.init()
  //   cycleAnalyzer.init()
  //   scalarAnalyzer.init()
  //   memoryAnalyzer.init()
  //   contentionAnalyzer.init()

  //   config.setV(-1)
  //   scalarAnalyzer.silence()
  //   memoryAnalyzer.silence()
  //   contentionAnalyzer.silence()
  //   areaAnalyzer.silence()
  //   cycleAnalyzer.silence()
  //   areaAnalyzer.run(program)(mtyp(program.tp))
  // }

  def run(): Unit = {
  //   //println(s"[$threadId] Started.")

  //   while(isAlive) {
  //     val requests = workQueue.take() // Blocking dequeue

  //     if (requests.nonEmpty) {
  //       //println(s"[$threadId] Received batch of ${requests.length}. Working...")
  //       try {
  //         val result = run(requests)
  //         //println(s"[$threadId] Completed batch of ${requests.length}. ${workQueue.size()} items remain in the queue")
  //         outQueue.put(result) // Blocking enqueue
  //       }
  //       catch {case e: Throwable =>
  //         println(s"[$threadId] Encountered error while running: ")
  //         println(e.getMessage)
  //         e.getStackTrace.foreach{line => println(line) }
  //         isAlive = false
  //       }
  //     }
  //     else {
  //       //println(s"[$threadId] Received kill signal, terminating!")
  //       requestStop()
  //     } // Somebody poisoned the work queue!
  //   }

  //   //println(s"[$threadId] Ending now!")
  //   hasTerminated = true
  }

  // def run(requests: Seq[BigInt]): Array[String] = {
  //   val array = new Array[String](requests.size)
  //   var i: Int = 0
  //   requests.foreach{pt =>
  //     state.resetErrors()
  //     indexedSpace.foreach{case (domain,d) => domain.set( ((pt / prods(d)) % dims(d)).toInt ) }

  //     //println(params.map{case p @ Bound(c) => p.name.getOrElse(p.toString) + s": $c" }.mkString(", "))

  //     val (area, runtime) = evaluate()
  //     val valid = area <= capacity && !state.hadErrors // Encountering errors makes this an invalid design point
  //     val time = System.currentTimeMillis() - START

  //     // Only report the area resources that the target gives maximum capacities for
  //     array(i) = space.map(_.value).mkString(",") + "," + area.seq(areaHeading:_*).mkString(",") + "," + runtime + "," + valid + "," + time

  //     i += 1
  //   }
  //   array
  // }

  // private def evaluate(): (Area, Long) = {
  //   if (PROFILING) resetClock()
  //   scalarAnalyzer.rerun(accel, program)
  //   if (PROFILING) endBnd()

  //   memoryAnalyzer.run()
  //   if (PROFILING) endMem()

  //   contentionAnalyzer.run()
  //   if (PROFILING) endCon()

  //   areaAnalyzer.rerun(accel, program)
  //   if (PROFILING) endArea()

  //   cycleAnalyzer.rerun(accel, program)
  //   if (PROFILING) endCycles()
  //   (areaAnalyzer.totalArea, cycleAnalyzer.totalCycles)
  // }

}
