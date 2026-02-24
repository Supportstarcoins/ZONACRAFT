#version 120

uniform sampler2D texture;

varying vec2 textureCoord;
varying vec3 brightnessOut;
varying float alphaOut;
varying float burnOut;

void main(){
    vec4 texel0 = texture2D(texture, textureCoord);
	float alphac = texel0.a * alphaOut * (1-burnOut);
	vec3 fgc = vec3(texel0.r*(brightnessOut.r + (1-brightnessOut.r)*burnOut), texel0.g*(brightnessOut.g + (1-brightnessOut.g)*burnOut), texel0.b*(brightnessOut.b + (1-brightnessOut.b)*burnOut)) * alphaOut;	
	gl_FragColor = vec4(fgc, alphac);
}