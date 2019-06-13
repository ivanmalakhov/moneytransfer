package com.revolut.service.processing;


import com.revolut.dto.ResponseMessage;
import com.revolut.dto.ResponseStatus;

/**
 * Abstract class for other processing stage.
 */
public abstract class ProcessingStage {
  /**
   * Next stage.
   */
  private ProcessingStage nextStage;

  /**
   * Builds chains of ProcessingStage objects.
   *
   * @param next - next chain
   * @return ProcessingStage
   */
  public ProcessingStage linkWith(final ProcessingStage next) {
    this.nextStage = next;
    return this.nextStage;
  }

  /**
   * Subclasses will implement this method with concrete operation.
   *
   * @param data - request
   * @return ResponseMessage
   */
  public abstract ResponseMessage performOperation(StageData data);

  /**
   * Runs operation on the next object in chain or ends traversing if we're in
   * last object in chain.
   *
   * @param data - request
   * @return ResponseMessage
   */
  protected ResponseMessage performNextOperation(final StageData data) {
    if (nextStage == null) {
      ResponseMessage responseMessage = new ResponseMessage();
      responseMessage.setStatus(ResponseStatus.BAD_REQUEST);
      return responseMessage;
    }
    return nextStage.performOperation(data);
  }
}
