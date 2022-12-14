package by.bsuir.akg.service;

import by.bsuir.akg.entity.Vector;

public class PhongShader {

    private final static Vector light_position = new Vector(50.0, 80.0, 200.0);

    private final static Vector diffuse_albedo = new Vector(0.5, 0.2, 0.7);
    private final static Vector specular_albedo = new Vector(0.9, 0.9, 0.9);
    private final static Vector ambient = new Vector(0.05, 0.05, 0.05);
    private final static double specular_power = 128.0;

    public static Vector getPhongColorWithTexture(Vector position, Vector normal, Vector diffuseColor, Vector mirror){
        Vector N = normal.normalize3();
        Vector L = (light_position.minus3(position)).normalize3();
//        if(L.getX() < 0 || L.getZ() <0 || L.getY()<0)
//            System.out.println(L.getX() + "   " + L.getY() + "    " + L.getZ());
        Vector V = (new Vector(-position.getX(), -position.getY(), -position.getZ())).normalize3();
        Vector R = (reflect(L, N)).normalize3();
//        if(R.getX() < 0 || R.getZ() <0 || R.getY()<0)
//            System.out.println(R.getX() + "   " + R.getY() + "    " + R.getZ());
        double kd = Math.max(dot(N, L), 0.0);
        if(kd < 0)
            System.out.println("kjns");
        Vector diffuse = new Vector(
                diffuseColor.getX() * kd,
                diffuseColor.getY() * kd,
                diffuseColor.getZ() * kd);
        double ks = Math.pow(Math.max(dot(R, V), 0.0), specular_power);
        Vector specular = new Vector(
                mirror.getX() * ks,
                mirror.getX() * ks,
                mirror.getX() * ks);
        Vector color = (diffuseColor.plus3(diffuse)).plus3(specular);
        return new Vector(
                Math.min(color.getX(), 1.0),
                Math.min(color.getY(), 1.0),
                Math.min(color.getZ(), 1.0));
    }
    public static Vector getPhongColor(Vector position, Vector normal) {
        Vector N = normal.normalize3();
        Vector L = (light_position.minus3(position)).normalize3();
        Vector V = (new Vector(-position.getX(), -position.getY(), -position.getZ())).normalize3();
        Vector R = (reflect(L, N)).normalize3();
        double kd = Math.max(dot(N, L), 0.0);
        Vector diffuse = new Vector(
                diffuse_albedo.getX() * kd,
                diffuse_albedo.getY() * kd,
                diffuse_albedo.getZ() * kd);
        double ks = Math.pow(Math.max(dot(R, V), 0.0), specular_power);
        Vector specular = new Vector(
                specular_albedo.getX() * ks,
                specular_albedo.getY() * ks,
                specular_albedo.getZ() * ks);
        Vector color = (ambient.plus3(diffuse)).plus3(specular);
        return new Vector(
                Math.min(color.getX(), 1.0),
                Math.min(color.getY(), 1.0),
                Math.min(color.getZ(), 1.0));
    }

    private static Vector reflect(Vector vector, Vector normal) {
        double velocity = dot(normal, vector);
        return new Vector(
                (-vector.getX()) + 2 * velocity * normal.getX(),
                (-vector.getY()) + 2 * velocity * normal.getY(),
                (-vector.getZ()) + 2 * velocity * normal.getZ());
    }

    private static double dot(Vector v1, Vector v2) {
        return v1.getX() * v2.getX() + v1.getY() * v2.getY() + v1.getZ() * v2.getZ();
    }
}
