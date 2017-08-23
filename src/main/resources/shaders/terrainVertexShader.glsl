#version 430 core

layout (location = 0) in vec4 position;
layout (location = 1) in vec2 texCoord;
layout (location = 2) in vec3 normal;

layout (location = 10) uniform mat4 MVP;
layout (location = 11) uniform vec3 LIGHT_POSITION;
layout (location = 16) uniform mat4 MODEL_TRANSFORM;
layout (location = 17) uniform mat4 VIEW_TRANSFORM;
layout (location = 18) uniform mat4 PROJECTION_TRANSFORM;

out VS_OUT
{
	vec2 texCoord;
	vec3 surfaceNormal;
	vec3 toLight;
	vec3 toCamera;
	vec4 color;
} vs_out;


void main()
{
	vec4 worldPosition = MODEL_TRANSFORM * position;

	gl_Position = MVP * position;

	vs_out.texCoord = texCoord;
	vs_out.surfaceNormal = (MODEL_TRANSFORM * vec4(normal, 0.0)).xyz;
	vs_out.toLight = LIGHT_POSITION - worldPosition.xyz;
	vs_out.toCamera = (inverse(VIEW_TRANSFORM) * vec4(0.0,0.0,0.0,1.0)).xyz - worldPosition.xyz;
	vs_out.color = vec4(1.0, 1.0, 1.0, 1.0);
}
