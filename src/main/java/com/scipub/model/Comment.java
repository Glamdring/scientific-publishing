/**
 * Scientific publishing
 * Copyright (C) 2015-2016  Bozhidar Bozhanov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.scipub.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

/**
 * A comment at either a publication or a peer review
 * 
 * @author bozhanov
 */
@Entity
@Table(name = "comments")
@Audited
public class Comment extends BaseTimedEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    @ManyToOne
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private PublicationRevision publication;

    @ManyToOne
    @Audited(targetAuditMode=RelationTargetAuditMode.NOT_AUDITED)
    private PeerReviewRevision peerReview;

    @Lob
    private String content;

    @Column(nullable = false)
    private boolean inline;
    
    @Column(nullable = false)
    private int inlineSelectionStart;
    
    @Column(nullable = false)
    private int inlineSelectionLength;
    
    @Column(nullable = false)
    private boolean addressed;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public PublicationRevision getPublication() {
        return publication;
    }

    public void setPublication(PublicationRevision publication) {
        this.publication = publication;
    }

    public PeerReviewRevision getPeerReview() {
        return peerReview;
    }

    public void setPeerReview(PeerReviewRevision peerReview) {
        this.peerReview = peerReview;
    }

    public boolean isInline() {
        return inline;
    }

    public void setInline(boolean inline) {
        this.inline = inline;
    }

    public int getInlineSelectionStart() {
        return inlineSelectionStart;
    }

    public void setInlineSelectionStart(int inlineSelectionStart) {
        this.inlineSelectionStart = inlineSelectionStart;
    }

    public int getInlineSelectionLength() {
        return inlineSelectionLength;
    }

    public void setInlineSelectionLength(int inlineSelectionLength) {
        this.inlineSelectionLength = inlineSelectionLength;
    }

    public boolean isAddressed() {
        return addressed;
    }

    public void setAddressed(boolean addressed) {
        this.addressed = addressed;
    }
}
