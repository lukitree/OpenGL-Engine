#version 430 core

layout (location = 12) uniform vec3 LIGHT_COLOR = vec3(0,0,0);
layout (location = 13) uniform vec3 SKY_COLOR;
layout (location = 14) uniform float SHINEDAMPER = 1;
layout (location = 15) uniform float REFLECTIVITY = 0;
layout (location = 19) uniform float AMBIENT_LIGHT_LEVEL = 0.2;

layout (binding = 0) uniform sampler2D bgTexture;
layout (binding = 1) uniform sampler2D rTexture;
layout (binding = 2) uniform sampler2D gTexture;
layout (binding = 3) uniform sampler2D bTexture;
layout (binding = 4) uniform sampler2D blendMap;

in VS_OUT
{
	vec2 texCoord;
	vec3 surfaceNormal;
	vec3 toLight;
	vec3 toCamera;
	float visibility;
} fs_in;

out vec4 out_color;

void main()
{
	vec4 blendMapColor = texture(blendMap, fs_in.texCoord);
	float bgTextureAmount = 1 - (blendMapColor.r + blendMapColor.g + blendMapColor.b);
	vec2 tiledCoords = fs_in.texCoord * 40;
	vec4 bgTextureColor = texture(bgTexture, tiledCoords) * bgTextureAmount;
	vec4 rTextureColor = texture(rTexture, tiledCoords) * blendMapColor.r;
	vec4 gTextureColor = texture(gTexture, tiledCoords) * blendMapColor.g;
	vec4 bTextureColor = texture(bTexture, tiledCoords) * blendMapColor.b;
	vec4 color = bgTextureColor + rTextureColor + gTextureColor + bTextureColor;

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

	out_color = vec4(diffuse,1.0) * color + vec4(finalSpecular, 1.0);
	out_color = mix(vec4(SKY_COLOR,1.0), out_color, fs_in.visibility);
}
