#version 430 core

out vec4 color;

in VS_OUT
{
	vec2 texCoord;
	vec4 color;
	vec3 normal;
} fs_in;

uniform sampler2D texture;

void main()
{
	//color = (texture(texture, fs_in.texCoord) + (fs_in.color - 2.75));
	color = (texture(texture, fs_in.texCoord) / (fs_in.color * 0.25));
}
