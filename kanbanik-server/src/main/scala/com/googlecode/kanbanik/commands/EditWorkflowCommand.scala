package com.googlecode.kanbanik.commands
import com.googlecode.kanbanik.dto.shell.EditWorkflowParams
import com.googlecode.kanbanik.dto.shell.VoidParams
import com.googlecode.kanbanik.builders.WorkflowitemBuilder
import com.googlecode.kanbanik.model.WorkflowitemScala
import org.bson.types.ObjectId
import scala.util.control.Breaks.break
import scala.util.control.Breaks.breakable
import com.googlecode.kanbanik.model.KanbanikEntity
import com.mongodb.casbah.commons.MongoDBObject
import com.googlecode.kanbanik.dto.BoardDto
import com.googlecode.kanbanik.model.BoardScala
import com.googlecode.kanbanik.dto.WorkflowitemDto

class EditWorkflowCommand extends ServerCommand[EditWorkflowParams, VoidParams] with KanbanikEntity {

  lazy val workflowitemBuilder = new WorkflowitemBuilder

  def execute(params: EditWorkflowParams): VoidParams = {

    val parentDto = params.getParent()
    val currenDto = params.getCurrent()
    val nextOfCurrentDto = params.getCurrent().getNextItem()
    val contextDto = params.getContext();
    
    val currentEntity = workflowitemBuilder.buildEntity(currenDto)

    if (contextDto != null) {
    	currentEntity.store(Some(WorkflowitemScala.byId(new ObjectId(contextDto.getId()))))  
    } else {
      currentEntity.store
    }

    updateBoard(currenDto, contextDto, currentEntity)

    new VoidParams
  }

  /**
   * The board.workflowitems can contain only workflowitems which has no parent (e.g. top level entities)
   * This method ensures it.
   */
  private def updateBoard(currentDto: WorkflowitemDto, contextDto: WorkflowitemDto, currentEntity: WorkflowitemScala) {
    val board = BoardScala.byId(new ObjectId(currentDto.getBoard().getId()))
    val isInBoard = findIfIsInBoard(board, currentEntity)
    val hasNewParent = contextDto != null

    if (isInBoard && hasNewParent) {
      if (board.workflowitems.isDefined) {
        board.workflowitems = Some(board.workflowitems.get.filter(_.id != currentEntity.id))
        board.store
      }
    } else if (!isInBoard && !hasNewParent) {
      if (board.workflowitems.isDefined) {
        board.workflowitems = Some(currentEntity :: board.workflowitems.get)
        board.store
      } else {
        board.workflowitems = Some(List(currentEntity))
        board.store
      }
    }
  }

  private def findIfIsInBoard(board: BoardScala, workflowitem: WorkflowitemScala): Boolean = {

    if (!board.workflowitems.isDefined) {
      return false
    }

    board.workflowitems.get.foreach(item => {
      if (item.id.get == workflowitem.id.get) {
        return true
      }
    })

    false
  }

}