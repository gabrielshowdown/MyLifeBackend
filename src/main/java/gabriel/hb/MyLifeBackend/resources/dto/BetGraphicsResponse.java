package gabriel.hb.MyLifeBackend.resources.dto;

import java.util.List;
import gabriel.hb.MyLifeBackend.repositories.projection.ParityGroupProjection;
import gabriel.hb.MyLifeBackend.repositories.projection.RepetitionGroupProjection;

public class BetGraphicsResponse {
    private List<ParityGroupProjection> paritiesPieData;
    private List<RepetitionGroupProjection> repetitionsPieData;
    private Long exactParityHits;
    private Long exactRepetitionHits;

    public BetGraphicsResponse(List<ParityGroupProjection> paritiesPieData, 
                               List<RepetitionGroupProjection> repetitionsPieData, 
                               Long exactParityHits, Long exactRepetitionHits) {
        this.paritiesPieData = paritiesPieData;
        this.repetitionsPieData = repetitionsPieData;
        this.exactParityHits = exactParityHits;
        this.exactRepetitionHits = exactRepetitionHits;
    }

    // Gerar Getters e Setters
    public List<ParityGroupProjection> getParitiesPieData() { return paritiesPieData; }
    public void setParitiesPieData(List<ParityGroupProjection> paritiesPieData) { this.paritiesPieData = paritiesPieData; }
    public List<RepetitionGroupProjection> getRepetitionsPieData() { return repetitionsPieData; }
    public void setRepetitionsPieData(List<RepetitionGroupProjection> repetitionsPieData) { this.repetitionsPieData = repetitionsPieData; }
    public Long getExactParityHits() { return exactParityHits; }
    public void setExactParityHits(Long exactParityHits) { this.exactParityHits = exactParityHits; }
    public Long getExactRepetitionHits() { return exactRepetitionHits; }
    public void setExactRepetitionHits(Long exactRepetitionHits) { this.exactRepetitionHits = exactRepetitionHits; }
}