package art.soft.utils;

import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

/**
 *
 * @author Артём Святоха
 */
public class Transform {

    public float addX, addY, addZ;

    public float scaleX = 1f, scaleY = 1f, scaleZ = 1f;

    public Vector3 axis = Vector3.Z;
    public float angle;
    //public float angleX, angleY, angleZ, angleW;

    Matrix4 matrix4;
    Matrix3 matrix3;

    public Transform() {}

    void initTransform() {
        Quaternion q = new Quaternion(axis, angle);
        matrix4 = new Matrix4();
        matrix4.set(addX, addY, addZ,
                q.x,//angleX * MathUtils.degreesToRadians,
                q.y,//angleY * MathUtils.degreesToRadians,
                q.z,//angleZ * MathUtils.degreesToRadians,
                q.w,//angleW * MathUtils.degreesToRadians,
                scaleX, scaleY, scaleZ);
        //
        matrix3 = new Matrix3();
        matrix3.setToRotation(axis, - angle);
    }
}
