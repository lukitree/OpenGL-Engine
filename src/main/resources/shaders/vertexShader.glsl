#version 430 core

layout (location = 0) in vec4 position;
layout (location = 1) in vec2 texCoord;
layout (location = 2) in vec3 normal;
layout (location = 10) uniform mat4 MVP;

out VS_OUT
{
	vec2 texCoord;
	vec4 color;
	vec3 normal;
} vs_out;


void main()
{
	gl_Position = MVP * position;

	vs_out.color = vec4(gl_Position.zzz, 1.0);
	vs_out.texCoord = texCoord;
	vs_out.normal = normal;
}
