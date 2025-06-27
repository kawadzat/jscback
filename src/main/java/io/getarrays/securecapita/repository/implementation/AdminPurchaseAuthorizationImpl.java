package io.getarrays.securecapita.repository.implementation;


import io.getarrays.securecapita.domain.AdminPurchaseAuthorization;
import io.getarrays.securecapita.repository.AdminPurchaseAuthorizationRepository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

public class AdminPurchaseAuthorizationImpl implements AdminPurchaseAuthorizationRepository {

    private NamedParameterJdbcTemplate jdbc;





    RowMapper<AdminPurchaseAuthorization>rowMapper=((rs, rowNum) -> {

          AdminPurchaseAuthorization AdminPurchaseAuthorization=new AdminPurchaseAuthorization();
        AdminPurchaseAuthorization adminPurchaseAuthorization=new AdminPurchaseAuthorization();
        adminPurchaseAuthorization.setId(rs.getLong("id"));
        adminPurchaseAuthorization.setRequestingDepartment(rs.getString("requesting_department"));
        adminPurchaseAuthorization.setStation(rs.getString("station"));
        adminPurchaseAuthorization.setStation(rs.getString("station"));
        adminPurchaseAuthorization.setStation(rs.getString("station"));
        adminPurchaseAuthorization.setDate(rs.getDate("date"));
        adminPurchaseAuthorization.setReason(rs.getString("reason"));
        adminPurchaseAuthorization.setBudgetLine(rs.getBigDecimal("budgetLine"));
        adminPurchaseAuthorization.setDescription(rs.getString("description"));
        adminPurchaseAuthorization.setQuantity(rs.getInt("quantity"));
        adminPurchaseAuthorization.setUnitprice(rs.getBigDecimal("unitprice"));
        adminPurchaseAuthorization.setEstimatedValue(rs.getBigDecimal("estimatedValue"));
        adminPurchaseAuthorization.setComments(rs.getString("comments"));

     return AdminPurchaseAuthorization;



    });




    @Override
    public List list() {
        return null;
    }

    @Override
    public AdminPurchaseAuthorization create(AdminPurchaseAuthorization data) {
        return null;
    }

    @Override
    public AdminPurchaseAuthorization get(Long id) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}
