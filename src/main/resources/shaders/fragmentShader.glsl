#version 430 core

layout (location = 12) uniform vec3 LIGHT_COLOR = vec3(0,0,0);
layout (location = 13) uniform bool HAS_TEXTURE = false;
layout (location = 14) uniform float SHINEDAMPER = 1;
layout (location = 15) uniform float REFLECTIVITY = 0;
layout (location = 19) uniform float AMBIENT_LIGHT_LEVEL = 0.2;
layout (location = 21) uniform vec3 SKY_COLOR;

uniform sampler2D modelTexture;

in VS_OUT
{
	vec2 texCoord;
	vec3 surfaceNormal;
	vec3 toLight;
	vec3 toCamera;
	float visibility;
} fs_in;

out vec4 out_color;

const vec4 DEFAULT_COLOR = vec4(1.0, 1.0, 1.0, 1.0);

void main()
{
	vec4 color;
	if(HAS_TEXTURE) color = texture2D(modelTexture, fs_in.texCoord) ;
	else color = DEFAULT_COLOR;

	if(color.a < 0.5) discard;

	vec3 unitSurfaceNormal = normalize(fs_in.surfaceNormal);
	vec3 unitToLight = normalize(fs_in.toLight);

	float brightness = max(dot(unitSurfaceNormal, unitToLight), AMBIENT_LIGHT_LEVEL);

	vec3 diffuse = brightness * LIGHT_COLOR;

	vec3 unitToCamera = normalize(fs_in.toCamera);
	vec3 lightDirection = -unitToLight;
	vec3 reflectedLightDirection = reflect(lightDirection, unitSurfaceNormal);

	float specularFactor = max(dot(reflectedLightDirection, unitToCamera), 0.0);
	float dampedFactor = pow(specularFactor, SHINEDAMPER);
	vec3 finalSpecular = dampedFactor * REFLECTIVITY * LIGHT_COLOR;

	out_color = vec4(vec4(diffuse,1.0) * color + vec4(finalSpecular, 1.0));
	out_color = mix(vec4(SKY_COLOR,1.0), out_color, fs_in.visibility);
}
