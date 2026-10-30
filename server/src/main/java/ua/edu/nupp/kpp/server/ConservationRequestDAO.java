package ua.edu.nupp.kpp.server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConservationRequestDAO {
    public void add(ConservationRequest r) throws SQLException {
        String sql = "INSERT INTO conservation_request(inspection_id,specialist_id,state,f1,f2,f3,risk_index,description) VALUES(?,?,?,?,?,?,?,?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            bind(p, r);
            p.executeUpdate();
        }
    }
    public List<ConservationRequest> getAll() throws SQLException {
        List<ConservationRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM conservation_request ORDER BY id";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet r = p.executeQuery()) {
            while (r.next()) {
                list.add(map(r));
            }
        }
        return list;
    }
    public ConservationRequest getById(long id) throws SQLException {
        String sql = "SELECT * FROM conservation_request WHERE id=?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, id);
            try (ResultSet r = p.executeQuery()) {
                return r.next() ? map(r) : null;
            }
        }
    }
    public void update(ConservationRequest r) throws SQLException {
        String sql = "UPDATE conservation_request SET inspection_id=?,specialist_id=?,state=?,f1=?,f2=?,f3=?,risk_index=?,description=? WHERE id=?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            bind(p, r);
            p.setLong(9, r.getId());
            p.executeUpdate();
        }
    }
    public void updateState(long id, ConservationRequestState state) throws SQLException {
        String sql = "UPDATE conservation_request SET state=? WHERE id=?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, state.name());
            p.setLong(2, id);
            p.executeUpdate();
        }
    }
    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM conservation_request WHERE id=?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, id);
            p.executeUpdate();
        }
    }
    private void bind(PreparedStatement p, ConservationRequest r) throws SQLException {
        p.setLong(1, r.getInspectionId());
        p.setLong(2, r.getSpecialistId());
        p.setString(3, r.getState().name());
        p.setDouble(4, r.getF1());
        p.setDouble(5, r.getF2());
        p.setDouble(6, r.getF3());
        p.setDouble(7, r.getRiskIndex());
        p.setString(8, r.getDescription());
    }
    private ConservationRequest map(ResultSet r) throws SQLException {
        return new ConservationRequest(
                r.getLong("id"),
                r.getLong("inspection_id"),
                r.getLong("specialist_id"),
                ConservationRequestState.valueOf(r.getString("state")),
                r.getDouble("f1"),
                r.getDouble("f2"),
                r.getDouble("f3"),
                r.getDouble("risk_index"),
                r.getString("description"));
    }
}