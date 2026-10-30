package ua.edu.nupp.kpp.server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SpecialistDAO {
    public void add(Specialist s) throws SQLException {
        String sql = "INSERT INTO specialist(full_name,position,specialization) VALUES(?,?,?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            bind(p, s);
            p.executeUpdate();
        }
    }
    public List<Specialist> getAll() throws SQLException {
        List<Specialist> list = new ArrayList<>();
        String sql = "SELECT * FROM specialist ORDER BY id";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet r = p.executeQuery()) {
            while (r.next()) {
                list.add(map(r));
            }
        }
        return list;
    }
    public Specialist getById(long id) throws SQLException {
        String sql = "SELECT * FROM specialist WHERE id=?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, id);
            try (ResultSet r = p.executeQuery()) {
                return r.next() ? map(r) : null;
            }
        }
    }
    public void update(Specialist s) throws SQLException {
        String sql = "UPDATE specialist SET full_name=?,position=?,specialization=? WHERE id=?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            bind(p, s);
            p.setLong(4, s.getId());
            p.executeUpdate();
        }
    }
    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM specialist WHERE id=?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, id);
            p.executeUpdate();
        }
    }
    private void bind(PreparedStatement p, Specialist s) throws SQLException {
        p.setString(1, s.getFullName());
        p.setString(2, s.getPosition());
        p.setString(3, s.getSpecialization());
    }
    private Specialist map(ResultSet r) throws SQLException {
        return new Specialist(
                r.getLong("id"),
                r.getString("full_name"),
                r.getString("position"),
                r.getString("specialization"));
    }
}
