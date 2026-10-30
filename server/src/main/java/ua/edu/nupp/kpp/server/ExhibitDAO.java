package ua.edu.nupp.kpp.server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExhibitDAO {
    public void add(Exhibit e) throws SQLException {
        String sql = "INSERT INTO exhibit(inventory_code,name,material,hall,material_sensitivity,exhibition_significance) VALUES(?,?,?,?,?,?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            bind(p, e);
            p.executeUpdate();
        }
    }
    public List<Exhibit> getAll() throws SQLException {
        List<Exhibit> list = new ArrayList<>();
        String sql = "SELECT * FROM exhibit ORDER BY id";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet r = p.executeQuery()) {
            while (r.next()) {
                list.add(map(r));
            }
        }
        return list;
    }
    public Exhibit getById(long id) throws SQLException {
        String sql = "SELECT * FROM exhibit WHERE id=?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, id);
            try (ResultSet r = p.executeQuery()) {
                return r.next() ? map(r) : null;
            }
        }
    }
    public void update(Exhibit e) throws SQLException {
        String sql = "UPDATE exhibit SET inventory_code=?,name=?,material=?,hall=?,material_sensitivity=?,exhibition_significance=? WHERE id=?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            bind(p, e);
            p.setLong(7, e.getId());
            p.executeUpdate();
        }
    }
    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM exhibit WHERE id=?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, id);
            p.executeUpdate();
        }
    }
    private void bind(PreparedStatement p, Exhibit e) throws SQLException {
        p.setString(1, e.getInventoryCode());
        p.setString(2, e.getName());
        p.setString(3, e.getMaterial());
        p.setString(4, e.getHall());
        p.setDouble(5, e.getMaterialSensitivity());
        p.setDouble(6, e.getExhibitionSignificance());
    }
    private Exhibit map(ResultSet r) throws SQLException {
        return new Exhibit(
                r.getLong("id"),
                r.getString("inventory_code"),
                r.getString("name"),
                r.getString("material"),
                r.getString("hall"),
                r.getDouble("material_sensitivity"),
                r.getDouble("exhibition_significance"));
    }
}