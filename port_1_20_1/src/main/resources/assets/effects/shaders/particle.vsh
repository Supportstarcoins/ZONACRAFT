#version 120

attribute vec4 vertexPosition;
attribute vec3 modelPosition;
attribute vec3 brightness;
attribute float rotation;
attribute float textureCoordsIdFloat;
attribute float size;
attribute float alpha;
attribute float burn;

varying vec2 textureCoord;
varying vec3 brightnessOut;
varying float alphaOut;
varying float burnOut;

uniform vec3 rotationVec;
uniform mat4 billboardRotMatrix;
uniform float[256] textureCoords;

mat4 rotationMatrix(vec3 axis, float angle)
{
    axis = normalize(axis);
    float s = sin(angle);
    float c = cos(angle);
    float oc = 1.0 - c;
    
    return mat4(oc * axis.x * axis.x + c,           oc * axis.x * axis.y - axis.z * s,  oc * axis.z * axis.x + axis.y * s,  0.0,
                oc * axis.x * axis.y + axis.z * s,  oc * axis.y * axis.y + c,           oc * axis.y * axis.z - axis.x * s,  0.0,
                oc * axis.z * axis.x - axis.y * s,  oc * axis.y * axis.z + axis.x * s,  oc * axis.z * axis.z + c,           0.0,
                0.0,                                0.0,                                0.0,                                1.0);
}

void main(){
	vec4 vertexPositionScaled = vec4(vertexPosition.x * size, vertexPosition.y  * size, vertexPosition.z * size, vertexPosition.w);
	gl_Position = gl_ProjectionMatrix * gl_ModelViewMatrix * (vec4(modelPosition, 1) + 
	(billboardRotMatrix*rotationMatrix(rotationVec, radians(rotation)))*vertexPositionScaled);
	
	float xTextureCoord;
	float yTextureCoord;
	int textureCoordsId = int(textureCoordsIdFloat);
	if (vertexPosition.x < 0){
		xTextureCoord = textureCoords[textureCoordsId*4];
	} else {
		xTextureCoord = textureCoords[textureCoordsId*4+2];
	}
	if (vertexPosition.y > 0){
		yTextureCoord = textureCoords[textureCoordsId*4+1];
	} else {
		yTextureCoord = textureCoords[textureCoordsId*4+3];
	}
	textureCoord = vec2(xTextureCoord, yTextureCoord);
	brightnessOut = brightness;
	alphaOut = alpha;
	burnOut = burn;
}