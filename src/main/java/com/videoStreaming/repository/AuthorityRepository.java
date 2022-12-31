package com.videoStreaming.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.videoStreaming.model.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
