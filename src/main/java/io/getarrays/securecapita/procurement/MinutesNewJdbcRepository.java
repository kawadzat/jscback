package io.getarrays.securecapita.procurement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

//@Repository
//@RequiredArgsConstructor
//@Slf4j

//public class MinutesNewJdbcRepository implements MinutesRepository {
//
//    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
//
//       @Override
//    public MinutesEntity saveFile(@RequestParam("file") MultipartFile file, MinutesEntity pEntity) {
//
//        log.info("Entering Inside Insert Statement ------ ");
//
//        if (!file.isEmpty()) {
//            try {
//                byte[] bytes = file.getBytes();
//                String sql = INSERT_MINUTES_REQUEST;
//
//                KeyHolder keyHolder = new GeneratedKeyHolder();
//                MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
//
//                mapSqlParameterSource.addValue("fullNames", pEntity.);
//
//            mapSqlParameterSource.addValue("requestingDepartment", pEntity.getRequestingDepartment());
//                mapSqlParameterSource.addValue("departmentCode", pEntity.getDepartmentCode());
//                mapSqlParameterSource.addValue("requestReason", pEntity.getRequestReason());
//
//
//                mapSqlParameterSource.addValue("name", file.getOriginalFilename());
//                mapSqlParameterSource.addValue("type", file.getContentType());
//                mapSqlParameterSource.addValue("profileImage", bytes);
//                namedParameterJdbcTemplate.update(sql, mapSqlParameterSource, keyHolder);
//                pEntity.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
//
//                String email = pEntity.getEmailAddress();
//                String subject = "Purchase Request  Sent for you";
//                String message = "Hello " + pEntity.getId() + " for Product Name " + pEntity.getQuantity() + ", "
//                        + "\n A Purchase Request Email Verification Was Sent To \n" + pEntity.getEmailAddress();
//                emailService.sendEmail(email, subject, message);
//            } catch (IOException e) {
//                log.error(
//                        "Exception for Create saveFile method under PurchaseRequestNewJdbcRepository class '{}' \n '{}'",
//                        e, e.getMessage());
//            }
//
//            return pEntity;
//        }
//        return pEntity;
//    }























//}
