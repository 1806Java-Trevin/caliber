package com.revature.caliber.beans;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "CALIBER_NOTE")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Note implements Serializable {

	private static final long serialVersionUID = -4960654794116385953L;

	@Id
	@Column(name = "NOTE_ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTE_ID_SEQUENCE")
	@SequenceGenerator(name = "NOTE_ID_SEQUENCE", sequenceName = "NOTE_ID_SEQUENCE")
	private int noteId;

	@Length(min = 0, max = 4000)
	@Column(name = "NOTE_CONTENT")
	private String content;

	@Min(value = 1)
	@Column(name = "WEEK_NUMBER")
	private short week;

	@Column(name = "BATCH_ID", nullable = true)
	private Integer batch;

	/**
	 * Will be null if the note is overall batch feedback
	 */
	@Column(name = "TRAINEE_ID", nullable = true)
	private Integer trainee;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "NOTE_TYPE")
	private NoteType type;

	@Column(name = "IS_QC_FEEDBACK", nullable = false)
	private boolean qcFeedback;

	@Enumerated(EnumType.STRING)
	@Column(name = "QC_STATUS", nullable = true)
	private QCStatus qcStatus;

	public Note() {
		super();
	}

	public int getNoteId() {
		return noteId;
	}

	public void setNoteId(int noteId) {
		this.noteId = noteId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public short getWeek() {
		return week;
	}

	public void setWeek(short week) {
		this.week = week;
	}

	public Integer getBatch() {
		return batch;
	}

	public void setBatch(Integer batch) {
		this.batch = batch;
	}

	public Integer getTrainee() {
		return trainee;
	}

	public void setTrainee(Integer trainee) {
		this.trainee = trainee;
	}

	public NoteType getType() {
		return type;
	}

	public void setType(NoteType type) {
		this.type = type;
	}

	public boolean isQcFeedback() {
		return qcFeedback;
	}

	public void setQcFeedback(boolean qcFeedback) {
		this.qcFeedback = qcFeedback;
	}

	public QCStatus getQcStatus() {
		return qcStatus;
	}

	public void setQcStatus(QCStatus qcStatus) {
		this.qcStatus = qcStatus;
	}

	@Override
	public String toString() {
		return "Note [noteId=" + noteId + ", content=" + content + ", week=" + week + ", trainee=" + trainee + ", type="
				+ type + ", qcFeedback=" + qcFeedback + ", qcStatus=" + qcStatus + "]";
	}

}
