package io.getarrays.securecapita.repository.implementation;

import io.getarrays.securecapita.domain.PurchaseRequisition;
import io.getarrays.securecapita.exception.ApiException;
import io.getarrays.securecapita.query.PurchaseQuery;
import io.getarrays.securecapita.repository.PurchaseRequestsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;




import static java.util.Map.of;



@Repository
@RequiredArgsConstructor
@Slf4j

public class PurchaseRequestsJdbc implements PurchaseRequestsRepository<PurchaseRequisition> {




    private final NamedParameterJdbcTemplate jdbc;



    RowMapper<PurchaseRequisition> rowMapper = (rs, rowNum) -> {
        PurchaseRequisition purchaseRequest = new PurchaseRequisition();
        purchaseRequest.setId(rs.getLong("id"));
        purchaseRequest.setDate(rs.getDate("Date"));
        purchaseRequest.setDepartmentCode(rs.getInt("departmentCode"));
        purchaseRequest.setReason(rs.getString("reason"));
        purchaseRequest.setItemNumber(rs.getInt("itemNumber"));
        purchaseRequest.setItemDescription(rs.getString("itemDescription"));
        purchaseRequest.setUnitPrice(rs.getInt("unitPrice"));
        purchaseRequest.setQuantity(rs.getInt("quantity"));
        purchaseRequest.setEstimatedValue(rs.getInt("estimatedValue"));
        purchaseRequest.setReceiverEmail(rs.getString("receiverEmail"));
        purchaseRequest.setSignature(rs.getString("signature"));
        return purchaseRequest;
    };

    @Override
    public List<PurchaseRequisition> list() {
        try {
            String query = "SELECT * FROM purchaseRequests";
            List<PurchaseRequisition> purchaseRequests = jdbc.query(query,rowMapper) ;                        //query(query, new UserRowMapper());
            return purchaseRequests;
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while retrieving the list of users. Please try again.");
        }

    }


    @Override
    public PurchaseRequisition create(PurchaseRequisition purchaseRequests) {
        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource parameters = getSqlParameterSource(purchaseRequests);
        jdbc.update(PurchaseQuery.INSERT_PurchaseRequisition_REQUEST_QUERY,parameters, holder);
        return purchaseRequests;
    }

    private SqlParameterSource getSqlParameterSource(PurchaseRequisition purchaseRequisition) {
        return new MapSqlParameterSource()
                .addValue("id",purchaseRequisition.getId())
                .addValue("date", purchaseRequisition.getDate())
                .addValue("departmentCode", purchaseRequisition.getDepartmentCode())
                .addValue("reason", purchaseRequisition.getReason())
                .addValue("itemNumber", purchaseRequisition.getItemNumber())
                .addValue("itemDescription", purchaseRequisition.getItemDescription())
                .addValue("unitPrice", purchaseRequisition.getUnitPrice())

                .addValue("unitPrice", purchaseRequisition.getUnitPrice())
                .addValue("quantity",purchaseRequisition.getQuantity())


                .addValue("estimatedValue",purchaseRequisition.getEstimatedValue())
                .addValue("receiverEmail", purchaseRequisition.getReceiverEmail())
                .addValue("signature", purchaseRequisition.getSignature());
    }


    @Override
    public PurchaseRequisition get(Long id) {
        try {

            return jdbc.queryForObject(PurchaseQuery.INSERT_PurchaseRequisition_REQUEST_QUERY, of("id", id),rowMapper);

        } catch (EmptyResultDataAccessException exception) {
            throw new ApiException("No PURCHASE REQUESTS found by id: " + id);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred. Please try again.");
        }
    }

//    @Override
//    public void update(PurchaseRequests purchaseRequests, Long id) {
//
//    }

//    @Override
//    public void update(PurchaseRequests purchaseRequests, Long id) {
//        try {
//
//            String UPDATE_PURCHASEREQUESTS_BY_PURCHASEREQUEST_ID = "UPDATE PURCHASEREQUEST SET productName=?,Date=?,productCode=? WHERE id = :purchaserequestsId";
//
//            jdbcTemplate.update(UPDATE_PURCHASEREQUESTS_BY_PURCHASEREQUEST_ID, purchaseRequests.getProductName(),purchaseRequests.getDate(),purchaseRequests.getProductCode(),id);
//            return;
//
//        }
//        catch (Exception exception) {
//            log.error(exception.getMessage());
//            throw new ApiException("An error occurred. Please try again.");
//        }
//
//    }



    @Override
    public boolean delete(Long id) {
        try {
            String DELETE_FROM_PURCHASEREQUESTS_BY_PURCHASEREQUEST_ID = "DELETE FROM PURCHASEREQUEST WHERE id = :purchaserequestwId";
            jdbc.update(DELETE_FROM_PURCHASEREQUESTS_BY_PURCHASEREQUEST_ID, Collections.singletonMap("purchaserequestsId", id));
            return true;
        }
        catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred. Please try again.");
        }

    }


}
