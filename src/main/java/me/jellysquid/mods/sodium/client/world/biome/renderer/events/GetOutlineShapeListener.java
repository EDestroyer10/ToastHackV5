package me.jellysquid.mods.sodium.client.world.biome.renderer.events;

import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Event;
import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Listener;

import java.util.ArrayList;

public interface GetOutlineShapeListener extends Listener
{

	void onGetOutlineShape(GetOutlineShapeEvent event);

	class GetOutlineShapeEvent extends Event<GetOutlineShapeListener>
	{

		private BlockView view;
		private BlockPos pos;
		private ShapeContext context;
		private VoxelShape returnValue;

		public GetOutlineShapeEvent(BlockView view, BlockPos pos, ShapeContext context)
		{
			this.view = view;
			this.pos = pos;
			this.context = context;
		}

		public BlockView getView()
		{
			return view;
		}

		public BlockPos getPos()
		{
			return pos;
		}

		public ShapeContext getContext()
		{
			return context;
		}

		public VoxelShape getReturnValue()
		{
			return returnValue;
		}

		public void setReturnValue(VoxelShape returnValue)
		{
			this.returnValue = returnValue;
		}

		@Override
		public void fire(ArrayList<GetOutlineShapeListener> listeners)
		{
			for (GetOutlineShapeListener listener : listeners)
			{
				listener.onGetOutlineShape(this);
			}
		}

		@Override
		public Class<GetOutlineShapeListener> getListenerType()
		{
			return GetOutlineShapeListener.class;
		}
	}

}
