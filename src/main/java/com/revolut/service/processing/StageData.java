package com.revolut.service.processing;

import com.revolut.dto.AbstractDTO;
import com.revolut.service.processing.params.Params;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Class for transfer entity between stages.
 */
@Data
@AllArgsConstructor
public class StageData {
  /**
   * Request.
   */
  private String requestData;
  /**
   * One of the DTO.
   */
  private AbstractDTO dto;
  /**
   * Payment parameters.
   */
  private Params params;
}
