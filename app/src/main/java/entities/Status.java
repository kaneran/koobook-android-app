package entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(foreignKeys = @ForeignKey(entity = Audit.class, parentColumns = "auditId", childColumns = "audit_auditId", onDelete = ForeignKey.CASCADE))
public class Status {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int statusId;

    @ColumnInfo(name = "audit_auditId")
    private int auditId;

    @ColumnInfo(name = "status")
    private String status;

    @ColumnInfo(name = "timeStamp")
    private String timestamp;

    @ColumnInfo(name = "reason")
    private String reason;

    public Status(int statusId, int auditId, String status, String timestamp, String reason) {
        this.statusId = statusId;
        this.auditId = auditId;
        this.status = status;
        this.timestamp = timestamp;
        this.reason = reason;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getAuditId() {
        return auditId;
    }

    public void setAuditId(int auditId) {
        this.auditId = auditId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
