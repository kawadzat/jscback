package io.getarrays.securecapita.query;

public class PurchaseQuery {
    public static final String SELECT_PURCHASEREQUESTS_BY_ID_QUERY ="SELECT * FROM PurchaseRequest WHERE id = :id";
    public static final String INSERT_PurchaseRequisition_REQUEST_QUERY = "INSERT INTO PurchaseRequisition (date, departmentCode, reason, itemNumber, ItemDescription, unitPrice, quantity, estimatedValue, receiverEmail, signature) VALUES (:date, :departmentCode, :reason, :itemNumber, :itemDescription, :unitPrice, :quantity, :estimatedValue, :receiverEmail, :signature)";

}
