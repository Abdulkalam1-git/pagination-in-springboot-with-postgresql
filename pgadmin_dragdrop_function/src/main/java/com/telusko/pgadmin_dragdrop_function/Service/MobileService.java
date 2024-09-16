package com.telusko.pgadmin_dragdrop_function.Service;

import com.telusko.pgadmin_dragdrop_function.Model.Mobile;
import com.telusko.pgadmin_dragdrop_function.Repository.MobileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MobileService {

    @Autowired
    private MobileRepository mobileRepository;
   // add data
    public Mobile saveMobile(Mobile mobile) {
        return mobileRepository.save(mobile);
    }

    // Method to filter by created date range and apply pagination
    public Page<Mobile> getMobilesByCreatedDateRange(LocalDateTime startCreatedDate, LocalDateTime endCreatedDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return mobileRepository.findByCreatedDateBetween(startCreatedDate, endCreatedDate, pageable);
    }

    // Method to filter by updated date range and apply pagination
    public Page<Mobile> getMobilesByUpdatedDateRange(LocalDateTime startUpdatedDate, LocalDateTime endUpdatedDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return mobileRepository.findByUpdatedDateBetween(startUpdatedDate, endUpdatedDate, pageable);
    }

    // Get all mobiles with pagination (no filter)
//    public Page<Mobile> getAllMobilesPaginated(int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//
//        return mobileRepository.findAll(pageable);
//    }
//    public Page<Mobile> getAllMobilesPaginated(int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        // Fetch the paginated result
//        Page<Mobile> pagedResult = mobileRepository.findAll(pageable);
//
//        // Sort the content of the page by priority
//        List<Mobile> sortedList = pagedResult.getContent().stream()
//                .sorted((p1, p2) -> Integer.compare(p1.getPriority(), p2.getPriority()))
//                .toList();
//
//        // Return a new Page object with the sorted list
//        return new PageImpl<>(sortedList, pageable, pagedResult.getTotalElements());
//    }
    // Update a mobile by id
    public Mobile updateMobile(Long id, Mobile updatedMobile) {
        // Find the mobile by id
        Optional<Mobile> optionalMobile = mobileRepository.findById(id);
        if (optionalMobile.isPresent()) {
            Mobile existingMobile = optionalMobile.get();
            // Update fields of the existing mobile with the new data
            existingMobile.setMobileName(updatedMobile.getMobileName());
            existingMobile.setPriority(updatedMobile.getPriority());
            existingMobile.setUpdatedDate(LocalDateTime.now()); // set updated date to now

            // Save and return the updated mobile
            return mobileRepository.save(existingMobile);
        } else {
            throw new RuntimeException("Mobile not found with id: " + id);
        }
    }
    public Page<Mobile> getAllMobilesPaginated(int page, int size) {
        // Fetch all mobiles from the repository
        List<Mobile> allMobiles = mobileRepository.findAll();

        // Sort all mobiles by priority
        List<Mobile> sortedList = allMobiles.stream()
                .sorted((p1, p2) -> Integer.compare(p1.getPriority(), p2.getPriority()))
                .toList();

        // Apply pagination
        int start = Math.min((int) PageRequest.of(page, size).getOffset(), sortedList.size());
        int end = Math.min((start + size), sortedList.size());
        List<Mobile> pagedList = sortedList.subList(start, end);

        // Create and return the Page object
        return new PageImpl<>(pagedList, PageRequest.of(page, size), sortedList.size());
    }


//    public List<Mobile> getAllProductsOrderedByPriority() {
//        return mobileRepository.findAll().stream()
//                .sorted((p1, p2) -> Integer.compare(p1.getPriority(), p2.getPriority()))
//                .toList();
//    }
}
