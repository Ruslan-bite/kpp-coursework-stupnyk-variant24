package ua.edu.nupp.kpp.server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InspectionDAO {
    public void add(Inspection i) throws SQLException {
        String sql = "INSERT INTO inspection(exhibit_id,inspection_date,damage_severity,notes) VALUES(?,?,?,?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            bind(p, i);
            p.executeUpdate();
        }
    }
    public List<Inspection> getAll() throws SQLException {
        List<Inspection> list = new ArrayList<>();
        String sql = "SELECT * FROM inspection ORDER BY id";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet r = p.executeQuery()) {
            while (r.next()) {
                list.add(map(r));
            }
        }
        return list;
    }
    public Inspection getById(long id) throws SQLException {
        String sql = "SELECT * FROM inspection WHERE id=?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, id);
            try (ResultSet r = p.executeQuery()) {
                return r.next() ? map(r) : null;
            }
        }
    }
    public void update(Inspection i) throws SQLException {
        String sql = "UPDATE inspection SET exhibit_id=?,inspection_date=?,damage_severity=?,notes=? WHERE id=?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            bind(p, i);
            p.setLong(5, i.getId());
            p.executeUpdate();
        }
    }
    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM inspection WHERE id=?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, id);
            p.executeUpdate();
        }
    }
    private void bind(PreparedStatement p, Inspection i) throws SQLException {
        p.setLong(1, i.getExhibitId());
        p.setString(2, i.getInspectionDate());
        p.setDouble(3, i.getDamageSeverity());
        p.setString(4, i.getNotes());
    }
    private Inspection map(ResultSet r) throws SQLException {
        return new Inspection(
                r.getLong("id"),
                r.getLong("exhibit_id"),
                r.getString("inspection_date"),
                r.getDouble("damage_severity"),
                r.getString("notes"));
    }
}
