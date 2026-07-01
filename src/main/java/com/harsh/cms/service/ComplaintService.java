package com.harsh.cms.service;

import com.harsh.cms.exception.ComplaintNotFoundException;
import com.harsh.cms.model.Complaint;
import com.harsh.cms.repository.ComplaintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import java.time.LocalDateTime;
import java.util.List;
 
@Service
public class ComplaintService {
 
    @Autowired
    private ComplaintRepository complaintRepository;
 
    public Complaint addComplaint(Complaint complaint) {
        return complaintRepository.save(complaint);
    }
 
    public Complaint updateComplaint(Long id, Complaint updatedComplaint) {
        Complaint existing = complaintRepository.findById(id)
                .orElseThrow(() -> new ComplaintNotFoundException("Complaint not found with id: " + id));
 
        existing.setTitle(updatedComplaint.getTitle());
        existing.setDescription(updatedComplaint.getDescription());
        existing.setStatus(updatedComplaint.getStatus());
        
        if("COMPLETED".equals(updatedComplaint.getStatus()) && existing.getClosedOn() == null) {
        	existing.setClosedOn(LocalDateTime.now());
        }else if(!"COMPLETED".equals(updatedComplaint.getStatus())){
        	existing.setClosedOn(null);
        }
        	
        existing.setRemarks(updatedComplaint.getRemarks());
 
        return complaintRepository.save(existing);
    }
 
    public void deleteComplaint(Long id) {
        Complaint existing = complaintRepository.findById(id)
                .orElseThrow(() -> new ComplaintNotFoundException("Complaint not found with id: " + id));
        complaintRepository.delete(existing);
    }
 
    public List<Complaint> loadComplaints() {
        return complaintRepository.findAll();
    }
 
    public Complaint getComplaintById(Long id) {
        return complaintRepository.findById(id)
                .orElseThrow(() -> new ComplaintNotFoundException("Complaint not found with id: " + id));
    }
 
    public List<Complaint> getComplaintsByStatus(String status) {
        return complaintRepository.findByStatus(status);
    }
 
    public List<Complaint> getComplaintsByCustomer(Long customerId) {
        return complaintRepository.findByCustomer_Id(customerId);
    }
 
    public List<Complaint> getComplaintsByEmployee(Long employeeId) {
        return complaintRepository.findByAssignedEmployee_Id(employeeId);
    }
 
    public List<Complaint> getComplaintsByDateRange(LocalDateTime start, LocalDateTime end) {
        return complaintRepository.findByCreatedDateBetween(start, end);
    }
 
    public Complaint updateComplaintRemarks(Long id, String remarks) {
        Complaint complaint = getComplaintById(id);
        complaint.setRemarks(remarks);
        return complaintRepository.save(complaint);
    }
}
 
